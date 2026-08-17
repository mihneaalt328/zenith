# ZENITH SkyPvP 2.2.0

Complete English SkyPvP core for a **Spigot 1.8.9** server.

## Branding
- Name: **ZENITH SkyPvP**
- IP shown in the plugin: **skypvp.zenithmc.net**
- Suggested store: **store.zenithmc.net**
- Palette: Aqua/Cyan + White + Dark Gray, with Gold for coins/crates.

## Included out of the box
- Finished scoreboard design with rank, online/max players, coins, kills, deaths, streak and best streak.
- Finished TAB header/footer and rank prefixes.
- Online player count in TAB and scoreboard.
- RankSystem hook for rank/prefix.
- Kits: Starter, VIP, MVP, Elite.
- Coin shop.
- Vote / Legendary / Zenith crates with weighted rewards.
- Daily rewards.
- Kill rewards and killstreak bonuses.
- Persistent kills/deaths/coins/streak data.
- `/stats`, `/top`, `/daily`, `/kit`, `/shop`, `/crates`.
- Combat tag and command blocking.
- Spawn-area protection around the Bukkit/Essentials spawn.
- Auto server announcements.
- English configuration/messages.

## EssentialsX spawn
Zenith does **not** implement `/setspawn` or `/spawn`.
Use EssentialsX / EssentialsSpawn:
1. Stand where players should spawn.
2. Run `/setspawn`.
3. Test `/spawn`.

Zenith's spawn protection uses the world's Bukkit spawn location, so keep the Essentials spawn aligned with the world spawn.

## Crates
Place your chest on the map, look at it, then:
`/zenith setcrate Vote`
`/zenith setcrate Legendary`
`/zenith setcrate Zenith`

Give keys with:
`/zenith givekey <player> <crate> [amount]`

## Recommended plugins
- EssentialsX
- EssentialsSpawn
- RankSystem
- ViaVersion
- WorldEdit (only if you need to paste the map)

## Map recommendation
For this project I recommend the free **Lirias SkyPvP Map** on Planet Minecraft. It is listed as 1.8+, approximately 300x300, and contains 19 PvP islands plus a spawn island, crate area, NPC spots, toplist spots and shop spots. The creator allows use on servers with credit.

Planet Minecraft page:
https://www.planetminecraft.com/project/skypvp-map-download-4715719/

When you send me the downloaded map/world, I can map its exact spawn, crate, shop and gameplay coordinates into the Zenith configuration.

## GitHub Actions
The repository includes `.github/workflows/build.yml` so GitHub can compile the plugin without installing Java/Maven on your PC.
