package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Champion;
import com.revature.utils.ConnectionUtil;

public class ChampionOracle implements ChampionDao {
	
	private static final Logger logger = LogManager.getLogger(ChampionOracle.class);
	private static ChampionOracle championOracle;
	final static ChampionDao championDao = ChampionOracle.getDao();
	
	private ChampionOracle() {
		
	}
	
	public static ChampionOracle getDao() {
		if (championOracle == null) {
			championOracle = new ChampionOracle();
		}
		return championOracle;
	}
	
	@Override
	public Optional<List<Champion>> getAllChampions() {
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			return Optional.empty();
		}
		try {
			String sql = "select * from champions";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<Champion> listOfChampions = new ArrayList<>();
			while (rs.next()) {
				listOfChampions.add(new Champion(rs.getInt("champion_id"), rs.getString("champion_name"),
						rs.getString("a1"), rs.getString("a2"), rs.getString("a3"), rs.getString("a4"),
						rs.getString("passive"), rs.getFloat("health")));
			}
			
			return Optional.of(listOfChampions);
		} catch (SQLException e) {
			logger.catching(e);
		}
		return null;
	}

//	@Override
//	public Optional<Champion> getChampionByName(String name) {
//		return null;
//		TODO: update from Quinn's
//		Connection con = ConnectionUtil.getConnection();
//
//		if (con == null) {
//			return Optional.empty();
//		}
//		try {
//			String sql = "select * from champions where champion_name = ?";
//			PreparedStatement ps = con.prepareStatement(sql);
//			ps.setString(0,name);
//			ResultSet rs = ps.executeQuery();
//
//			Champion champion = null;
//			while (rs.next()) {
//				champion = new Champion(rs.getInt("champion_id"), rs.getString("champion_name"),
//						rs.getString("a1"), rs.getString("a2"), rs.getString("a3"), rs.getString("a4"),
//						rs.getString("passive"), rs.getFloat("health"));
//			}
//			
//			if (champion == null) {
//				return null;
//			}
//			return champion;
//		} catch (SQLException e) {
//
//		}
//		return null;
//	}

	@Override
	public Optional<Champion> getChampionById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Champion> getChampionByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
