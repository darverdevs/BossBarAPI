# BossBarAPI
Adds the ability to display BossBars to eaglercraft with an API
## How to use:
There is one default command that BossBarAPI comes with and that is /bossbar which creates a bossbar with the text "Countdown" and the bar slowly goes down

Here is a guide to the API:
To create a BossBar do

```BossBar bar = new BossBar(player)```

This will create a bossbar instance but wont spawn in the bossbar itself to the player

To set the Bars health do

```bar.setBarHealth(int)```

To set the bar's text do:

```bar.setText(String)```

If these values are not set before the bossbar is displayed, they will default to 200 which is the bar's full health and "A Bossbar!"

there are also getters for these 2 methods

To display a bossbar, use

```bar.display()```

To delete the bossbar, do

```bar.delete```

There is also a `bar.getLocation` which returns the location of the Enderdragon but you will most likely never use this method

ok thanx byeee
