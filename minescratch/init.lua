win_stone = nil
points = 100

minetest.register_node("minescratch:win_stone", {
	description = "Winner Stone",
  tiles = {
    "default_chest_top.png",
    "default_chest_top.png",
    "default_chest_side.png",
		"default_chest_side.png",
    "default_chest_side.png",
    "default_chest_front.png"
  },
  paramtype2 = "facedir",
	is_ground_content = true,
	groups = {cracky=3, stone=1},
	legacy_mineral = false,
  on_place = minetest.rotate_node
})


minetest.register_on_punchnode(function(pos, node, puncher, pointed_thing)
	if node.name == "minescratch:win_stone" then
    local name = puncher:get_player_name()
    local player_point = points > 10 and points or 10
    points = points - 15
    minetest.chat_send_all(name .. " got " .. player_point .. " points")
  end
end)


minetest.register_on_chat_message(function(name, message)

    if message == "criar" then
        local player = minetest.get_player_by_name(name)
        local pos = player:getpos()
        local yaw = player:get_look_yaw()
        local distance = 5

        win_stone = minetest.add_item(pos, "minescratch:win_stone")
        minetest.chat_send_player(name, 'Tela Criada')
        return
    elseif command == "destruir" then
        win_stone:remove()
        win_stone = nil
        minetest.chat_send_player(name, "destruido")
    else
        minetest.chat_send_player(name, "comando nao encontrado: " .. message)
    end
end)
