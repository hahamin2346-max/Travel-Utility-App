def clean_txt(input_file, output_file):
    cleaned_lines = []

    with open(input_file, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()

            # 빈 줄 제거
            if not line:
                continue

            # 숫자로 시작하지 않으면 제거
            if not line[0].isdigit():
                continue

            cleaned_lines.append(line)

    with open(output_file, "w", encoding="utf-8") as f:
        for line in cleaned_lines:
            f.write(line + "\n")

    print(f"{len(cleaned_lines)}개 줄 저장 완료")


clean_txt(
    "word list.txt",
    "cleaned.txt"
)