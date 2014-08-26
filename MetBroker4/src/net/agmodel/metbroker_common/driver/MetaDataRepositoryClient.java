/**
 * MetaDataRepositoryClient
 * Copyright (C) 2012, 2013, 2014 
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

import net.agmodel.metbroker_common.weatherData.MetRequest;

/**
 * MetBrokerDataRepositoryClinet class generates a driver using properties file, 
 * installs/delets/uodates the driver to a MetBrokerDataRepositroyServer,
 * <br>
 * Using the driver, it update MetaData in XML format.
 * And connects a Data Source to get metadata to create metadata XML.
 * Store the XML in local strage and use it next execute
 * 
 * @see net.agmodel.metbroker_common.weatherData.MetRequest
 * @see net.agmodel.metbroker.server.MetaDataRepositoryServer
 * @see net.agmodel.metbroker_common.MetDriver
 */
public interface MetaDataRepositoryClient {
	
	/**
	 * 各ドライバのpropertiesファイルを読み込む。
	 * propertiesファイルのActionにしたがって、ドライバの削除、インストール、
	 * 更新を行うメソッドを呼出し、そのActionのコントロールを行う。
	 */
	public void getProperties();
	
	/**
	 * ドライバの削除処理を行う。
	 * propertiesファイルに対して対象ドライバの削除処理をする。
	 * @param driver 対象となるドライバ。
	 * @return 実行結果を返す。
	 */
	public int removeDriver(String driver);
	
	/**
	 * ドライバのインストール処理を行う。
	 * propertiesファイルに対して対象ドライバのインストール処理をする。
	 * 与えられたドライバ情報をpropertiesファイルに入力することでドライバのインストールを行う。
	 * @param driver 対象となるドライバ。
	 * @return 実行結果のステータスを返す。
	 */
	public int installDriver(String driver);
	
	/**
	 * ドライバの更新処理を行う。
	 * 与えられた新しいドライバ情報についてthis.removeDriver()を実行した後に
	 * this.installDriver()を実行することで更新処理とする。
	 * @param driver 対象となるドライバ。
	 * @return 実行結果のステータスを返す。
	 */
	public int reloadDriver(String driver);
	
	/**
	 * 指定されたドライバ情報をpropertiesから取得する。
	 * @param driver ドライバ名
	 * @return ドライバ情報
	 */
	public String[] getDriver(String driver);
	
	/**
	 * 指定されたドライバを使用して、MetaDataを取得する。
	 * MetaDataはXMLファイルとなる。
	 * @param driver ドライバ
	 * @return 取得したMetaData
	 */
	public String[] getMetaData(String driver);
	
	/**
	 * MetaDataの更新処理を行う。
	 * 指定したドライバを使用してMetaDataの更新処理を行う。
	 * @param driver
	 * @return 実行結果を返す。
	 */
	public int reloadMetaData(String driver);
	
}
