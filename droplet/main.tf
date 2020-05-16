provider "digitalocean" {
  # export DIGITALOCEAN_TOKEN="Your API TOKEN"
}

data "digitalocean_ssh_key" "ssh_key" {
  name = "blink"
}

resource "digitalocean_droplet" "dev" {
  image = "${var.ubuntu}"
  name = "dev-02"
  region = "${var.do_sgp1}"
  size = "s-1vcpu-1gb"
  ssh_keys = [data.digitalocean_ssh_key.ssh_key.id]
}
