#!/bin/bash
set -euo pipefail
project_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
log_file="$project_dir/logs/Mambetali_Aruzhan_system_report.log"
analysis_file="$project_dir/logs/Mambetali_Aruzhan_log_analysis.txt"

echo '-- Last five log lines --'
tail -n 5 "$log_file"
echo '-- Search for surname --'
grep -n 'Mambetali' "$log_file" | tee "$analysis_file"
echo "Search results saved in $analysis_file"
