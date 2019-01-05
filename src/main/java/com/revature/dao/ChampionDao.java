package com.revature.dao;

import java.util.List;
import java.util.Optional;

import com.revature.models.Champion;

public interface ChampionDao {
	Optional<List<Champion>> getAllChampions();
	Optional<Champion> getChampionByName(String name);
	Optional<Champion> getChampionById();
}
