/**
 * MetaDataRepositoryServer
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

import net.agmodel.metbroker_common.weatherData.MetRequest;

/**
 *  MetBrokerサーバ関連機能を提供する。
 *  MetaDataの更新要求をMetaDataRepositoryClinentへ行う。
 *  MetaDataRepositoryClientのデータドライバを使用してキャッシュの更新を行う。
 *  <br>
 *  リクエスト(MetRequest)を受け付けたらデータドライバを使用して、キャッシュから検索を行う。
 *  データドライバ情報はMetaDataRepositoryClientを使用する。
 *  <br>
 *  MetBrokerの稼働・停止のコントロールを行う。
 *  MetBrokerのサービスの設定用にpropertiesファイルを用意する。
 *  プロパティのフラグを使用して、稼働・停止を行う。
 *  
 *  @see net.agmodel.metbroker_common.driver.MetaDataRepositoryClient
 *  @see net.agmodel.metbroker_common.weatherData.MetRequest
 */
public interface MetaDataRepositoryServer {
	
	/**
	 * ドライバのリストを取得する。
	 * MetaDataCacheから全ドライバを取得し、返す。
	 * @return ドライバ一覧のリスト。
	 */
	public String[] getListDriver();
	
	/**
	 * 指定されたドライバを使用して、MetaDataの取得をMetaDataRepositoryClientに依頼する。
	 * @param driver ドライバ
	 * @return 取得したMetaData
	 */
	public String[] getMetaData(String driver);
	
	/**
	 * MetaDataの更新処理を行う。
	 * 指定したドライバを使用してMetaDataの更新処理をMetaDataRepositoryClientに依頼する。
	 * @param driver
	 * @return 実行結果を返す。
	 */
	public int reloadMetaData(String driver);
	
	/**
	 * 全MetaDataの更新処理を行う。
	 * ドライバ一覧を取得し、その全てに対して更新処理をそれぞれMetaDataRepositoryClientに依頼する。
	 * @return 実行結果を返す。
	 */
	public int reloadMetaDataAll();

	/**
	 * 設定用のpropertiesファイルを調べて、そのキーに設定内容を返す。
	 * 対応するキーがなければエラーコードを返す。
	 * @param key プロパティのキー
	 * @return 設定内容
	 */
	public String checkProperties(String key);
	
	/**
	 * MetBrokerを停止状態にする。
	 * プロパティの停止状態のフラグをON(true)にする。
	 */
	public void reposeMetBroker();
	
	/**
	 * MetBrokerを稼働状態にする。
	 * プロパティの停止状態のフラグをOFF(false)にする。
	 */
	public void relaseMetBroker();
	
	/**
	 * MetBrokerが稼働状態にあるかを検査する。
	 * プロパティの停止状態のフラグを返す。trueは停止中、falseは稼働中を表す。
	 * @return 稼働状態を返す。 trueは停止中、falseは稼働中を表す。
	 */
	public boolean checkRepose();
	
	/**
	 * ドライバーを利用して、リクエストの内容でキャッシュから検索を行う。
	 * エラーが生じた場合はそのエラー結果を返す。
	 * ユーザクライアントにはエラー内容は表示しない。
	 * 検索結果の言語はMetRequest.localeに従う。
	 * @param request リクエスト
	 * @param driver ドライバー
	 * @return 検索結果
	 */
	public String[] doRequestCache(MetRequest request, String driver);
	
	/**
	 * キャッシュの更新をする。
	 * ドライバーを使用してキャッシュの情報をMetaDataの内容でアップロードする。
	 * エラーが発生した場合はそのエラーを返す。
	 * ユーザクライアントにはエラー内容は表示しない。
	 * @param driver ドライバー
	 * @return 実行結果
	 */
	public int updateCache(String driver);
	
	/**
	 * 全てのキャッシュの更新をする。
	 * 全てのMetaDataの内容をキャッシュにアップロードする。
	 * プロパティからドライバーの一覧を取得し、それをもとに全てのキャッシュをアップロードする。
	 * @return 実行結果
	 */
	public int updateAllCache();
}
