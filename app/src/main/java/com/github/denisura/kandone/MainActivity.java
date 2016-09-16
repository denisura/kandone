package com.github.denisura.kandone;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.denisura.kandone.ui.dialog.SaveTaskDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.denisura.kandone.R.id.fab;

public class MainActivity extends SingleFragmentActivity {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    protected Fragment createFragment() {
        return MainActivityFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
    }

    @OnClick(fab)
    public void onFabCLick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
////        FragmentManager fm = getSupportFragmentManager();
////        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Some Title");
////        editNameDialogFragment.show(fm, "fragment_edit_name");
//
//
////        EditNameDialogFragment editNameDialogFragment = new EditNameDialogFragment();
//        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Some Title");
//        editNameDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
//        editNameDialogFragment.show(getSupportFragmentManager(), "fragment_edit_name");


        FragmentManager fragmentManager = getSupportFragmentManager();
        SaveTaskDialogFragment newFragment = SaveTaskDialogFragment.newAddInstance();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
