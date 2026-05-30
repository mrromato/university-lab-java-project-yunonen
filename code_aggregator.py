import os

# Имя итогового файла
OUTPUT_FILE = "project_context.txt"

# Расширения файлов, которые нам нужны
ALLOWED_EXTENSIONS = {'.java', '.properties', '.yml', '.yaml', '.xml', '.sql'}

# Папки-мусорщики, которые мы Обязательно игнорируем
IGNORE_DIRS = {'target', '.idea', '.git', '.mvn', 'node_modules'}

def collect_code():
    project_root = os.getcwd()
    files_saved = 0

    with open(OUTPUT_FILE, 'w', encoding='utf-8') as outfile:
        # 1. Сначала принудительно берем pom.xml из корня, если он есть
        pom_path = os.path.join(project_root, 'pom.xml')
        if os.path.exists(pom_path):
            outfile.write(f"=== START OF FILE: pom.xml ===\n")
            with open(pom_path, 'r', encoding='utf-8', errors='ignore') as f:
                outfile.write(f.read())
            outfile.write("\n=== END OF FILE: pom.xml ===\n\n")
            files_saved += 1

        # 2. Обходим все остальные папки и файлы
        for root, dirs, files in os.walk(project_root):
            # Фильтруем папки на лету, чтобы скрипт не заходил в target или .git
            dirs[:] = [d for d in dirs if d not in IGNORE_DIRS]

            for file in files:
                # Пропускаем корневой pom.xml, так как уже добавили его сверху
                if file == 'pom.xml' and root == project_root:
                    continue
                # Пропускаем сам скрипт сборщика и его результат
                if file in ['code_aggregator.py', OUTPUT_FILE]:
                    continue

                ext = os.path.splitext(file)[1]
                if ext in ALLOWED_EXTENSIONS:
                    full_path = os.path.join(root, file)
                    # Делаем красивый относительный путь (например: src/main/java/...)
                    rel_path = os.path.relpath(full_path, project_root)

                    outfile.write(f"=== START OF FILE: {rel_path} ===\n")
                    try:
                        with open(full_path, 'r', encoding='utf-8', errors='ignore') as f:
                            outfile.write(f.read())
                    except Exception as e:
                        outfile.write(f"// Ошибка чтения файла: {e}\n")
                    outfile.write(f"\n=== END OF FILE: {rel_path} ===\n\n")
                    files_saved += 1

    print(f"🎉 Успех! Собрано файлов: {files_saved}")
    print(f"📁 Весь код сохранен в: {os.path.abspath(OUTPUT_FILE)}")

if __name__ == "__main__":
    collect_code()