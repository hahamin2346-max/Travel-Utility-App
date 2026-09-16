import json

result = []

with open("cleaned.txt", "r", encoding="utf-8") as f:
    for line in f:
        # 탭으로 분리
        cols = [x.strip() for x in line.split("\t")]

        # 빈 문자열 제거
        cols = [x for x in cols if x]

        # 예:
        # ['1', 'be', '있다, 있다, 참석', '★★★★']

        if len(cols) < 4:
            continue

        result.append({
            "word": cols[1],
            "meaning": cols[2]
        })

with open("cefrWord.json", "w", encoding="utf-8") as f:
    json.dump(result, f, ensure_ascii=False, indent=2)

print(f"{len(result)}개 저장 완료")