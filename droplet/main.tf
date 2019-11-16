provider "digitalocean" {
  # export DIGITALOCEAN_TOKEN="Your API TOKEN"
}

resource "digitalocean_droplet" "dev" {
  image = "${var.ubuntu}"
  name = "dev-1"
  region = "${var.do_sgp1}"
  size = "512mb"
  ssh_keys = []
}
