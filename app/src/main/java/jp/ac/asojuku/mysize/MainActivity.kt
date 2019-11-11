package jp.ac.asojuku.mysize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    /*pushの演習コメント*/
    /*commit&pushの練習コメント*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        //入力値を端末内に保存
        //共有リファレンスのインスタンスを取得
        val pref = PreferenceManager.getDefaultSharedPreferences(this);
        //共有リファレンスのインスタンスから4つの保存済みの値を取り出す
        val editNeck = pref.getString("NECK","");//首回りの保存値を取得
        val editSleeve = pref.getString("SLEEVE","");//裄丈の保存値を取得
        val editWaist = pref.getString("WAIST","");//胴回りの保存値を取得
        val editInseam = pref.getString("INSEAM","");//股下の保存値を取得
        //取得した保存値で各入力欄の表示を上書き
        neck.setText(editNeck);//取得した首回りの値で表示を更新
        sleeve.setText(editSleeve);//取得した首回りの値で表示を更新
        waist.setText(editWaist);//取得した首回りの値で表示を更新
        inseam.setText(editInseam);//取得した首回りの値で表示を更新
        //保存ボタンが押された時の処理
        save.setOnClickListener{ onSaveTappde() }

        //身長アイコンボタンのクリック時の画面遷移を設定
        heightButton.setOnClickListener {
            //クリックされた時にOSが呼び出す処理を実装
            //インデントを設定(どこからどこまで情報)
            val intent = Intent(this,HeightActivity::class.java);
            //画面遷移メソッド（startActivity）を実行
            this.startActivity(intent);
        }
    }
    //保存ボタンが押されたらOSがコールバックする
    private fun onSaveTappde(){
        //画面表示の値を共有プリファレンスに保存する
        //共有リファレンスのインスタンスを取得
        val pref = PreferenceManager.getDefaultSharedPreferences(this);
        //共有プリファレンスを更新（editメソッド）
        pref.edit {//ラムダ式でインスタンスに対して引き続き処理を実行
            //首回りの入力値を共有プリファレンスに保存
            //ラムダ式なのでthisは省略可
            this.putString("NECK",neck.text.toString());
            this.putString("SLEEVE",sleeve.text.toString());//裄丈の入力値を共有プリファレンスに保存
            this.putString("WAIST",waist.text.toString());//胴回りの入力値を共有プリファレンスに保存
            this.putString("INSEAM",inseam.text.toString());//股下の入力値を共有プリファレンスに保存

        }
    }

}
