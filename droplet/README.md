# Get image slugs

```
% curl -s "https://api.digitalocean.com/v2/images?filter=global&per_page=100" -H "Authorization: Bearer $DIGITALOCEAN_TOKEN" | jq -r ".images[].slug"
```
