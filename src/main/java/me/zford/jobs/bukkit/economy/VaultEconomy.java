/**
 * Jobs Plugin for Bukkit
 * Copyright (C) 2011 Zak Ford <zak.j.ford@gmail.com>
 * Copyright (C) 2013 Simon Bastien-Filiatrault <root@gopoi.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.zford.jobs.bukkit.economy;

import me.zford.jobs.config.ConfigManager;
import me.zford.jobs.economy.Economy;

public class VaultEconomy implements Economy {
	private net.milkbowl.vault.economy.Economy vault;

	public VaultEconomy(net.milkbowl.vault.economy.Economy vault) {
		this.vault = vault;
	}

	@Override
    public synchronized boolean depositPlayer(String playerName, double money) {
    	
    	if (ConfigManager.getJobsConfiguration().getClosedEconomy()){
    		
    		if  (withdrawBank(money))  {
    			if (!vault.depositPlayer(playerName, money).transactionSuccess()) {
    				depositBank(money);			
    			} else {
    				return true;
    			}
    		}
    	} else return vault.depositPlayer(playerName, money).transactionSuccess();
    	
    	return false;
    }

	@Override
	public synchronized boolean withdrawPlayer(String playerName, double money) {
		
    	if (ConfigManager.getJobsConfiguration().getClosedEconomy()){
    		
    		if  (depositBank(money))  {
    			if (!vault.withdrawPlayer(playerName, money).transactionSuccess()) {
    				withdrawBank(money);			
    			} else {
    				return true;
    			}
    		}
    	} else 	return vault.withdrawPlayer(playerName, money).transactionSuccess();
    	return false;
	}

	@Override
	public String format(double money) {
		return vault.format(money);
	}
	
	private synchronized boolean withdrawBank(double money){
		return vault.withdrawPlayer(ConfigManager.getJobsConfiguration().getClosedEconomyAccount(), money).transactionSuccess();
	}
	
	private synchronized boolean depositBank(double money){
		return vault.depositPlayer(ConfigManager.getJobsConfiguration().getClosedEconomyAccount(), money).transactionSuccess();
	}
}
