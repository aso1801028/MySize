package jp.ac.asojuku.janken

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
    }

    //ここでグーチョキパーを表す定数を定義する
    val gu =0; val choki = 1; val pa = 2;

    override fun onResume() {
        super.onResume()
        //じゃんけんで選んだView部品（ボタン）のidをインデントから取り出す
        val id = intent.getIntExtra("MY_HAND",0);
        //前の画面で選んだ手を保持する定数を定義する
        val myHand:Int;
        //idの値によって処理を分岐、自分のじゃんけん画像を切り替える
        myHand = when(id){
            R.id.gu -> {myHandimage.setImageResource(R.drawable.gu);gu} //グー画像で上書き
            R.id.choki -> {myHandimage.setImageResource(R.drawable.choki);choki} //チョキ画像で上書き
            R.id.pa -> {myHandimage.setImageResource((R.drawable.pa));pa} //パー画像で上書き
            else -> gu;
        }
        //コンピュータの手をランダムに決める
        //val comHand =  (Math.random() * 3).toInt() //0か1か2がランダムに入る
        val comHand = getHannd()//メソッドで組み立てた手を採用する
        //コンピュータの手に合わせてコンピュータの画像を切り替える
        when(comHand){
            gu -> {comHandimage.setImageResource(R.drawable.com_gu)}
            choki -> {comHandimage.setImageResource(R.drawable.com_choki)}
            pa -> {comHandimage.setImageResource(R.drawable.com_pa)}
        }
        //勝敗を判定
        val gameresult = (comHand - myHand +3) % 3 //計算結果が　０引き分け　1自分の勝ち　2相手の勝ち
        //計算結果に合わせて勝敗メッセージを切り替え
        when(gameresult){
            0 -> {resultLavel.setText(R.string.result_draw)}
            1 -> {resultLavel.setText(R.string.result_win)}
            2 -> {resultLavel.setText(R.string.result_lose)}
        }
        //戻るボタンにタップされた時の処理
        backButton.setOnClickListener{this.finish()}//戻るボタンが押されたら結果画面を破棄する

        //勝敗とじゃんけんで出した手を保存する
        this.saveData(myHand,comHand,gameresult);//引数はユーザーの手、コンピュータの手、勝敗、それぞれの変数値
    }

    //ResultActivityクラスに勝敗データを保存するメソッドを追加する
    private fun saveData(myHand:Int, comHand:Int, gameResult:Int){
        //共有リファレンスを使う ①インスタンスを取得
        val pref = PreferenceManager.getDefaultSharedPreferences(this);
        //②値を取得する：キーを指定して値を取得する。該当するものがなければデフォルト値が返る
        val gameCount = pref.getInt("GAME_COUNT",0); //デフォルト値:0　//勝負の数
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT",0); //連勝数
        val lastComHand = pref.getInt("LAST_COM_HAND",0); //前回のコンピュータの手
        val lastGameResult = pref.getInt("LAST_GAME_RESULT",-1); //前回の勝敗
        //保存を始めていく。まず値を組み立てる
        //連勝数を更新
        val editWinningStreakCount = when{
            //前回勝って今回も勝ったら連勝+1を返す
            (lastGameResult ==2 && gameResult == 2) -> (winningStreakCount +1)
            else -> 0 //それ以外は0を返す
        }
            //③共有リファレンスの編集モードを取得
        val editor = pref.edit();
        //editorのメソッドをメソッドチェーンで呼び出し
        //editor.が変数みたいな役目をしているのでputIntをそのまま追加できる =メソッドチェーン
        editor.putInt("GAME_COUNT",gameCount+1) //勝負数
            .putInt("WINNING_STREAK_COUNT",winningStreakCount) //連勝数
            .putInt("LAST_MY_HAND",myHand) //ユーザーの前の手
            .putInt("LAST_COM_HAND",comHand) //コンピュータの前の手
            .putInt("BEFORE_LAST_COM_HAND",lastComHand) //コンピュータの前々の手
            .putInt("GAME_RESULT",gameResult) //勝敗
            .apply() //編集モードを確定して閉じる

    }
    //心理学ロジックを使ってグーチョキパーを決めるメソッド
    private  fun getHannd():Int{
        var hand = (Math.random() * 3).toInt(); //0～0.9までの乱数を3倍して整数にすると0か1か2
        //ここから心理学のロジックを使ってhandの値を上書きするかどうか処理する
        //共有リファレンスに保存したデータを取得するためにインスタンスを取得する
        val pref = PreferenceManager.getDefaultSharedPreferences(this);//インスタンスを取得
        //共有プリファレンスのインスタンス変数prefを使って保存値を取得していく
        val gameCount = pref.getInt("GAME_COUNT",0);//何回戦か
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT",0);//何連勝か
        val lastMyHand = pref.getInt("LAST_MY_HAND",0);//前のユーザーの手（グーチョキパー）
        val lastComHand = pref.getInt("LAST_COM_HAND",0);//前のコンピュータの手
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND",0)//2つ前のコンピュータの手
        val gameResult = pref.getInt("GAME_RESULT",-1);//前の勝ち負け

        //取得した保存値を使ってコンピューターの出す手（戻り値hand）を上書きする
        //前回が1回戦の時
        if(gameCount==1){
            if(gameResult==2){
                //前回の1回戦で、さらにコンピューターの勝ち（ユーザーの負け）
                //コンピュータの次の手を変える
                while (lastComHand==hand){//前回の手と今回の手が同じなら変える
                    hand = (Math.random()*3).toInt()//ランダムな値で更新
                }
            }else if(gameResult==1) {
                //前回が1回戦でさらにコンピュータの負け（ユーザーの勝ち）
                //相手が前回出した手に勝つ手を出す
                hand = (lastMyHand - 1 + 3) % 3;//グー（0）ならパー（2）、チョキ（1）ならグー（0）、パー（2）ならチョキ（1)

            }
        }else if(winningStreakCount>0){
            //連勝中の時
            if(beforeLastComHand==lastComHand){//同じ手で連勝した
                while(lastComHand==hand){//前回の手と今の手が同じなら変える
                    hand=(Math.random()*3).toInt()//ランダムな値で更新
                }
            }
        }


        return hand; //最終的な値を決定して返す
    }
}
