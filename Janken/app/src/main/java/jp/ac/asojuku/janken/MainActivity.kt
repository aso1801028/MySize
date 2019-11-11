package jp.ac.asojuku.janken

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent as Intent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //アプリが起動時に共有リファレンスを初期化する
        val pref = PreferenceManager.getDefaultSharedPreferences(this);
        //編集モードを取得
        pref.edit { this.clear() }
    }

    //追加したライフサイクルメソッド
    override fun onResume() {
        super.onResume()
        //ボタンが押されたら処理を呼び出し
        gu.setOnClickListener{onJankenButtonTapped(it); } //グーボタンが押されたらこの処理
        choki.setOnClickListener{onJankenButtonTapped(it);}
        pa.setOnClickListener{onJankenButtonTapped(it);}
    }
    //ボタンがクリックされたら呼び出される処理
    fun onJankenButtonTapped(view: View?){
        //画面遷移のためにインデントのインスタンスを作る
        val intent = Intent(this,ResultActivity::class.java);
        //インデントにおまけ情報（Extra）でどのボタン選んだか設定
        intent.putExtra("MY_HAND", view?.id);

        //OSにインデントを引き渡して画面遷移を実行してもらう
        startActivity(intent);
    }
}
