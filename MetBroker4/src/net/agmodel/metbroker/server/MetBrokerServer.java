/**
 * MetBrokerServer
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
package net.agmodel.metbroker.server;

/**
 * クライアントからリクエスト(MetRequest)を受け付けて、結果を返す。
 * MetaDataRepositoryServer,MetaDataRepositoryClinetから結果を得る。
 * <br>
 * リクエスト(MetRequest)を受け付けたら、ドライバを使用して、メタデータを検索し、その結果を返す。
 * 単一のMetBrokerとして動作するため、単一のドライバを使用して、メタデータの検索結果を返す。
 * <br>
 * 多言語化のため、MetDictionaryを使用して出力画面を生成する。
 * 
 * @see net.agmodel.metbroker_common.weatherData.MetRequest
 * @see MetaDataRepositoryServer
 * @see net.agmodel.metbroker_common.driver.MetaDataRepositoryClient
 * @see net.agmodel.metbroker_common.MetDriver
 * @see MetDictionary
 */
public interface MetBrokerServer {
	
	/**
	 * MetaData処理で使用するドライバーを指定する。
	 */
	public String driver = null;
	
	/**
	 * MetaData処理で使用するドライバーを設定する。
	 */
	public void setDriver();
	
	/**
	 * MetaData処理で使用するドライバー名を取得する。
	 * this.driverに設定されているドライバー名を返す。
	 * 設定されてい内場合はnullを返す。
	 * @return ドライバー名を返す
	 */
	public String getDriver();
	
	/**
	 * MetaDataに存在する気象データベース一覧を取得する。
	 * MetDictionaryを使用して、適切な言語で気象データベース名を返す。
	 * @return 気象データベース一覧
	 */
	public String[] listDatabase();
	
	/**
	 * MetaDataから地域一覧を取得する。
	 * MetDictionaryを使用して、適切な言語で地域名を返す。
	 * @return 地域一覧
	 */
	public String[] listRegion();
	
	/**
	 * MetaDataから観測地点一覧を取得する。
	 * MetDictionaryを使用して、適切な言語で観測地点を返す。
	 * @return 観測地点一覧
	 */
	public String[] listStaion();
	
	/**
	 * 検査条件一覧を作成する。
	 * MetDictionaryを使用して、適切な言語でデータ区分を返す。
	 * @return 検索条件一覧
	 */
	public String[] listRequest();
	
	/**
	 * MetaDataRepositoryServerを使用して検索を行う。
	 * 検索を行うにはMetRequestを使用する。
	 * 検索結果がエラーだった場合にはユーザクライアントに表示しないように留意する。
	 * @return　検索結果
	 */
	public String[] requestRepositoryServer();
	
	/**
	 * MetaDataRepositoryClientを使用して検索を行う。
	 * 検索にはMetRequestを使用する。
	 * 検索結果がエラーだった場合にはユーザクライアントに表示しないように留意する。
	 * @return 検索結果
	 */
	public String[] requestRepositoryClinet();
	
	/**
	 * リクエスト内容と、検索結果から画面表示を行う。
	 * 出力内容はLocaleで判別し、MetDictionaryを使用して各言語で表示する。
	 * XSS対策としてサニタイジングする。
	 */
	public void showDisplay();
	
	/**
	 * MetaDataRepositoryClientにMetaDataの更新要求を行う。
	 * 実際の更新処理はMetaDataRepositoryClientが行う。
	 */
	public void refreshMetaData();
	
	/**
	 * MetaDataRepositoryServerにキャッシュ更新要求を行う。
	 * 実際の更新処理はMetaDataRepositoryServerが行う。
	 */
	public void refreshCache();
	
}
