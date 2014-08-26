/**
 * MetDictionary
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

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * 多言語化のための用語集を操作するクラス。
 * ResouceBundleを使用して多言語化を実現する。
 * 用語集はpropertiesファイルを使用する。
 * デフォルト用語集はdictionary.propertiesとする。
 * 各言語はdictionary_jp.properties等となる。
 * <br>
 * 多言語化した場合のセキュリティ対策として、charset宣言等は明確にするように留意する。
 * <br>
 * @see <a href=http://www.atmarkit.co.jp/fjava/rensai4/programer05/programer05_2.html>参考外部サイト</a>
 */
public interface MetDictionary {
	/**
	 * ResourceBundle.Control,Localeを引数にしてリソースバンドルを取得する。
	 */
	public ResourceBundle resource = null;
	/**
	 * 言語のロケール。デフォルトで初期化する。
	 * MetRequstで指定がある場合は、setLocaleして上書きする。
	 */
	public Locale locale = Locale.getDefault();
	
	/**
	 * ResourceBundle.Controlのインスタンス。
	 * 主にキャッシュのコントロール定義を行うためにインスタンスを作成する。
	 */
	public Control control = null;
	
	/**
	 * BundleのキャッシュのTTL。
	 * long型で単位はミリ秒を指定する。
	 * デフォルト(-1L)はキャッシュを使用しない(TTL_DONT_CACHE)。
	 */
	public long cacheTTL = -1L;
	
	
	/**
	 * localeをもとに用語集から用語を取得する。
	 * @param data 取得するデータ
	 * @return 用語集から取得したデータ
	 */
	public String[] getWord(String data[]);
		
	/**
	 * localeをもとに用語集から用語を取得する。
	 * 一連の用語を連想配列で返す。
	 * @return 用語集から取得したデータの連想配列。
	 */
	public HashMap<String,String> getWordMap();
	
	/**
	 * キャッシュのTTLを設定する。
	 * -1Lはキャッシュを使用しない(TTL_DONT_CACHE)。
	 * -2Lはキャッシュの有効期限を無効にする。(TTL_NO_EXPIRATION_CONTROL)
	 * 0 は、バンドルがキャッシュから取得されるたびに検証される。
	 * @param ttl キャッシュのTTL値。単位はミリ秒
	 */
	public void setCacheTTL(long ttl);
	
	/**
	 * 現在設定されているキャッシュのTTLを返す。
	 * @return 現在設定されているキャッシュのTTL
	 */
	public long getCacheTTL();
	
	/**
	 * localeを設定する。
	 * クライアントのリクエストで設定されている場合はロケールを設定する。
	 * リクエストがない場合はデフォルト値が使用される。
	 * @param locale 設定するlocale
	 */
	public void setLocale(Locale locale);
	
	/**
	 * 現在設定されているlocaleを返す。
	 * @return 現在設定されているlocale
	 */
	public Locale getLocale();
	
	/**
	 * ResourceBundle.Controlのインスタンスを作成する。
	 * 作成時にキャッシュのTTLを変更するため、getTimeToLiveをオーバーライドする。
	 */
	public void createControl();
	
	/**
	 * 用語集が変更になって、キャッシュのアップデートが必要かどうかを判別する。
	 * @param data
	 * @return true(アップデートされている) or false(されていない)
	 */
	public Boolean isUpdateFrom(String data[]);
	
	/**
	 * キャッシュをクリアし、用語集をアップデートする。
	 * clearCache()を呼び出す。
	 * @return 実行結果
	 * @see java.util.ResourceBundle#clearCache()
	 */
	public int UpdateForm();
}
