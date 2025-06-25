This is a forge mod for Minecraft 1.12.2. 

Its objective is to allow the user to define custom triggers. A trigger associates with a forge event, for example, OnLivingHurt event, when that event happens, and when configurable conditions are met, actions can be performed. The conditions and actions can be configured. The context is built according to the event, for example, the OnLivingHurt event has two entities in its context, the entity that attacked, and the entity that has being attacked. The conditions are functions that recieves the elements in the context and return true or false, for example, the condition "EntityIsPlayer" would recieve a entity from the context and verify if it is a player.

An example of the json:


[
  {
    "name":  "on_living_hurt_speed_regen",
    "event": "on_living_hurt",
    "conditions": [
      { "id": "attacker_is_player" },
      {
        "id":     "attacker_has_potion_effect",
        "effect": "minecraft:speed",
        "value":  false
      }
    ],
    "actions": [
      {
        "id":        "apply_potion_effect",
        "potion":    "minecraft:regeneration",
        "duration":  100,
        "amplifier": 1,
        "target":    "attacker"
      }
    ]
  },
  {
    "name":  "random_chance_strength",
    "event": "on_living_hurt",
    "conditions": [
      { "id": "entity_is_player",
		"entity: "attacker"
	  },
      {
        "id":            "percent_chance",
        "chance": 30
      }
    ],
    "actions": [
      {
        "id":        "apply_potion_effect",
        "potion":    "minecraft:strength",
        "duration":  100,
        "amplifier": 1,
        "target":    "attacker"
      }
    ]
  }
]