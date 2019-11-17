# Datacenter regions

variable "do_sgp1" {
  description = "Digital Ocean Singapore Data Center 1"
  default     = "sgp1"
}

# Os

variable "ubuntu" {
  description = "Default LTS"
  default     = "ubuntu-19-10-x64"
}

variable "centos" {
  description = "Default Centos"
  default     = "centos-7-x64"
}