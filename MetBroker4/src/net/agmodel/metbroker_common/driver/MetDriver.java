/**
 * MetDriver
 * Copyright (C) 2012 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.agmodel.metbroker_common.driver;

import net.agmodel.metbroker_common.weatherData.StationMetRequest;


/**
 * MetBrokerドライバー
 *
 */
public interface MetDriver {
	
	/**
	 * requestをもとに検索を実施し、結果をresultとして返す。
	 * requestにはLocale情報が含まれているので、Localeにあったresultを返す。
	 * @param request 検索条件
	 * @param result 検索結果
	 */
	public void queryForStation(StationMetRequest request, StationDataSetProxy result);
	
	/**
	 * インスタンスの削除処理をする。
	 */
	public void destory();
	

}
