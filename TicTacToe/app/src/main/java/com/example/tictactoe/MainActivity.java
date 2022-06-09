package com.example.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tictactoe.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private Button [][] buttons;
    private TicTacToe tttGame;
    private TextView status;
    private ButtonGridAndTextView tttView;
    private ButtonHandler bh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        tttGame = new TicTacToe( );
        Point size = new Point( );
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        getWindowManager().getDefaultDisplay( ).getSize( size );
        int w = size.x / TicTacToe.SIDE;
        bh = new ButtonHandler( );
        tttView = new ButtonGridAndTextView( this, w,
                TicTacToe.SIDE, bh );
        tttView.setStatusText( "Play!!" );
        setContentView( tttView );
    }

    private class PlayDialog implements
            DialogInterface.OnClickListener {
        public void onClick( DialogInterface dialog, int id ) {
            if( id == -1 ) /* YES button */ {
                tttGame.resetGame( );
                tttView.enableButtons( true );
                tttView.resetButtons( );
                tttView.setStatusBackgroundColor( Color.GREEN );
                tttView.setStatusText( tttGame.result( ) );
            }
            else if( id == -2 ) // NO button
                MainActivity.this.finish( );
        }
    }

    public void enableButtons( boolean enabled ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setEnabled( enabled );
    }

    public void update(int row, int col) {
        Log.w( "MainActivity", "Inside update: " + row + ", " + col );
        int play = tttGame.play( row, col );
        if( play == 1 )
            buttons[row][col].setText( "X" );
        else if( play == 2 )
            buttons[row][col].setText( "O" );
        if( tttGame.isGameOver( ) ) {// game over
            status.setBackgroundColor( Color.RED );
            status.setText( tttGame.result());
            enableButtons(false);
            showNewGameDialog( );
        }
    }

    //Implement the ButtonHandler event
    private class ButtonHandler implements View.OnClickListener {
        public void onClick( View v ) {
            Log.w( "MainActivity", "Inside onClick, v = " + v );
            for( int row = 0; row < TicTacToe.SIDE; row++ )
                for( int column = 0; column < TicTacToe.SIDE; column++ )
                    if( tttView.isButton( ( Button ) v, row, column ) ) {
                        int play = tttGame.play( row, column );
                        if( play == 1 )
                            tttView.setButtonText( row, column, "X" );
                        else if( play == 2 )
                            tttView.setButtonText( row, column, "O" );
                        if( tttGame.isGameOver( ) ) {
                            tttView.setStatusBackgroundColor( Color.RED );
                            tttView.enableButtons( false );
                            tttView.setStatusText( tttGame.result( ) );
                            showNewGameDialog( ); // offer to play again
                        }
                    }
        }
    }

    public void showNewGameDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this);
        PlayDialog playAgain = new PlayDialog( );

        alert.setTitle( "This is fun" );
        alert.setMessage( "Play again?" );
        alert.setPositiveButton( "Yes", playAgain);
        alert.setNegativeButton( "No", playAgain );
        alert.show();
    }

}