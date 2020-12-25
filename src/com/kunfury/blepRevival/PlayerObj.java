package com.kunfury.blepRevival;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6607617821228194361L;

	public String DC;
	
	private String _uuid;
	private long _date;
	
	
	public PlayerObj(UUID uuid, Date date, DamageCause dc){
		_uuid = uuid.toString();
		_date = date.getTime();
		DC = dc.toString();
	}
	
	public UUID GetUUID() {
		
		return UUID.fromString(_uuid);
	}
	
	public Date GetDate() {
		Date tempDate = new Date(_date);
		
		return tempDate;
	}
	
}
