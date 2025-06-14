import os

def print_directory_tree(start_path, prefix=''):
    entries = sorted(os.listdir(start_path))
    entries_count = len(entries)
    
    for index, entry in enumerate(entries):
        full_path = os.path.join(start_path, entry)
        connector = "└── " if index == entries_count - 1 else "├── "
        print(prefix + connector + entry)

        if os.path.isdir(full_path):
            extension = "    " if index == entries_count - 1 else "│   "
            print_directory_tree(full_path, prefix + extension)

if __name__ == "__main__":
    script_dir = os.path.dirname(os.path.abspath(__file__))
    print(f"Directory Tree for: {script_dir}\n")
    print_directory_tree(script_dir)
