#!/bin/bash
set -euo pipefail

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
project_dir="$(dirname -- "$script_dir")"
log_file="$project_dir/logs/Mambetali_Aruzhan_system_report.log"
mkdir -p "$project_dir/logs"

{
  echo '========================================'
  echo 'System Report - Mambetali Aruzhan Imanaliyevna'
  echo "Date and time: $(date '+%Y-%m-%d %H:%M:%S %Z')"
  echo "Current username: $(whoami)"
  echo "Operating system: $(uname -s)"
  if [[ -r /etc/os-release ]]; then
    . /etc/os-release
    echo "Linux distribution: ${PRETTY_NAME:-unknown}"
  fi
  echo "Kernel version: $(uname -r)"
  echo '-- CPU --'
  if command -v lscpu >/dev/null 2>&1; then
    lscpu | sed -n '1,14p'
  else
    echo "CPU cores: $(getconf _NPROCESSORS_ONLN)"
  fi
  echo '-- Memory --'
  free -h
  echo '-- Disk usage --'
  df -h -x tmpfs -x devtmpfs
  echo '-- Available disk space for project --'
  df -h "$project_dir"
  echo '========================================'
} | tee -a "$log_file"

echo "Saved report to $log_file"
