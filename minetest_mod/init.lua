minetest.register_entity("minescratch:winnerPlace", {
    hp_max = 1000,
    physical = true,
    weight = 5,
    collisionbox = {-0.5,-0.5,-0.5, 0.5,0.5,0.5},
    visual = "mesh",
    visual_size = {x=1, y=1},
    mesh = "robot.x",
    textures = {"robot.png"},
    colors = {}, -- number of required colors depends on visual
    spritediv = {x=1, y=1},
    initial_sprite_basepos = {x=0, y=0},
    is_visible = true,
    makes_footstep_sound = false,
    automatic_rotate = false,
    groups = {immortal=1},
})