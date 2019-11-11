package jp.ac.asojuku.mysize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.view.View
import android.widget.*
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_height.*
import org.intellij.lang.annotations.JdkConstants
import java.util.function.Predicate
import java.util.prefs.PreferencesFactory

class HeightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_height)
    }

    // 再表示の時に呼ばれるライフサイクルのコールバックメソッドonResumeをoverride
    override fun onResume() {
        super.onResume()
        //スピナーにアイテムitem(選択肢)が選ばれた時のコールバックメソッドを設定
        spinner.onItemSelectedListener = //スピナーにアイテムを選んだ時の動きを持ったクラスの著名インスタンスを代入
            object :
                AdapterView.OnItemSelectedListener { //アイテムを選んだ時の動きを持ったクラスの継承クラスを定義して著名インスタンスにする
                override fun onItemSelected( //アイテムを選んだ時の処理
                    parent: AdapterView<*>?, //選択が発生したビュー（スピナーのこと）
                    View: View?, //選択されたビュー
                    position: Int, //選んだ選択肢が何番目か
                    id: Long //選択されたアイテムのID
                ) {
                    //選択値を取得するためにスピナーのインスタンスを取得する
                    val spinner = parent as? Spinner;
                    //選択値（170などの文字列）を取得
                    val item = spinner?.selectedItem as? String;
                    //取得した値を身長の値のテキストビューに上書きする
                    item?.let {
                        if (it.isNotEmpty()) {
                            height.text = it
                        }//itつまり身長の値が空文字でなければ、身長のテキストビュー（height）に代入
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) { //アイテムを何も選ばなかった時の処理

                    //何もしない
                }
            }

        //シークバーの処理を定義する
        //共有プリファレンスから慎重設定値を取得する（シークバー表示のため）
        val pref = PreferenceManager.getDefaultSharedPreferences(this);
        val heightVal = pref.getInt("HEIGHT", 160);//身長値を変数で保存
        height.text = heightVal.toString();//「身長」ラベルの値もこの取得地で上書き
        //シークバーの現在地（progress）も取得値で上書き
        seekBar.progress = heightVal;
        //  シークバーの値が変更されたらコールバックされるメソッドを持つ
        //匿名クラスのインスタンスを引き渡す
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, //値が変化したシークバーのインスタンス
                    progress: Int, //値が変化したシークバーの現在地
                    fromUser: Boolean //ユーザーが操作したか

                ) {
                    //ユーザーの指定値で「身長」の表示を変える
                    height.text = progress.toString(); //heightラベルの表示を上書き
                }


                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            }


        )

        //ラジオボタンの処理を実装
        //ラジオグループに選択された時に反応するコールバックメソッドを待機するリスナーを設定
        radioGroup.setOnCheckedChangeListener {
            //二つの引数（第一引数：ラジオボタンを選択グループ）、（第二引数：選択されたラジオボタンのid）を受けっと手実行する処理
                group, checkedId ->
            //「身長」ラベルを上書き(ラジオグループの選ばれたIDのボタンのText属性の値で上書き)
            height.text = findViewById<RadioButton>(checkedId).text;

        }


    }

    //  画面が閉じられるときに呼ばれるライフサイクルこーるバックメソッドをオーバーライド
    override fun onPause() {
        super.onPause()
        //身長の現在地を共有プリファレンスに保存する処理を実装
        //共有プリファレンスのインスタンスを取得
        val pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit {
            //身長ラベルの表示値を取得してStringに変換したのちIntに変換して保存
            this.putInt("HEIGHT", height.text.toString().toInt());
        }
    }
}


