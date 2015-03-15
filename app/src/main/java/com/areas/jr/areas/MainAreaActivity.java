package com.areas.jr.areas;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.software.shell.fab.ActionButton;

public class MainAreaActivity extends ActionBarActivity {

    public static final String TODO_GROUP_NAME = "ALL_TODOS";
    private static final int LOGIN_ACTIVITY_CODE = 100;
    private static final int EDIT_ACTIVITY_CODE = 200;

    //AreaAdapter areaListAdapter;
    ListView areaDesc;
    Button button_newArea;
    Activity main_activity;

    // Adapter for the Todos Parse Query
    private ParseQueryAdapter<Todo> todoListAdapter;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_area);

        main_activity = this;

        // add Todo subclass
        ParseObject.registerSubclass(Todo.class);

      /*  areaListAdapter = new AreaAdapter(this);

        areaDesc = (ListView)findViewById(R.id.listView1);
        areaDesc.setAdapter(areaListAdapter);*/

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "QhKfyUGLckCWZVun2rcciapdhZvdFW3eAi9v3INe", "AlPshZhdLdI8ao8tPU949MB1F4DTDTU5J1geXi9H");

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();

        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<Todo> factory = new ParseQueryAdapter.QueryFactory<Todo>() {
            public ParseQuery<Todo> create() {
                ParseQuery<Todo> query = Todo.getQuery();
                query.orderByDescending("createdAt");
                query.fromLocalDatastore();
                return query;
            }
        };
        // Set up the adapter
        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        todoListAdapter = new ToDoListAdapter(this, factory);

        // Attach the query adapter to the view
        ListView todoListView = (ListView) findViewById(R.id.listView1);
        todoListView.setAdapter(todoListAdapter);

        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Todo todo = todoListAdapter.getItem(position);
                openEditView(todo);
            }
        });

        button_newArea = (Button)findViewById(R.id.button_new_area);
        button_newArea.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(main_activity, NewAreaActivity.class);
                //i.putExtra("ID", todo.getUuidString());
                startActivityForResult(i, EDIT_ACTIVITY_CODE);
            }
        });

        // And then find it within the content view:
        ActionButton actionButton = (ActionButton) findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(main_activity, NewAreaActivity.class);
                //i.putExtra("ID", todo.getUuidString());
                startActivityForResult(i, EDIT_ACTIVITY_CODE);
            }
        });

        // To set an image (either bitmap, drawable or resource id):
        //actionButton.setImageBitmap(bitmap);
        //actionButton.setImageDrawable(getResource.getDrawable(R.drawable.fab_plus_icon));
        //actionButton.setImageResource(R.drawable.fab_plus_icon);


        //ParseQuery<Todo> query = Todo.getQuery();
        //query.fromPin(TODO_GROUP_NAME);
        //query.whereEqualTo("isDraft", false);

       /* try {
            List<Todo> objects = query.find();
            Log.d("Areas", "Areas " + objects.size() + " Areas");
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        loadFromParse();

        //Test Parse
        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        //testObject.saveInBackground();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // An OK result means the pinned dataset changed or
        // log in was successful
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_ACTIVITY_CODE) {
                // Coming back from the edit view, update the view
                todoListAdapter.loadObjects();

                //*** Añadido por mi
                syncTodosToParse();
            } else if (requestCode == LOGIN_ACTIVITY_CODE) {
                // If the user is new, sync data to Parse,
                // else get the current list from Parse
                if (ParseUser.getCurrentUser().isNew()) {
                    syncTodosToParse();
                } else {
                    loadFromParse();
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_area, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Check if we have a real user
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // Sync data to Parse
            syncTodosToParse();
            // Update the logged in label info
            updateLoggedInInfo();
        }
    }

    private void updateLoggedInInfo() {
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            //loggedInInfoView.setText(getString(R.string.logged_in,currentUser.getString("name")));
        } else {
          //  loggedInInfoView.setText(getString(R.string.not_logged_in));
        }
    }

    private void openEditView(Todo todo) {
       /* Intent i = new Intent(this, NewTodoActivity.class);
        i.putExtra("ID", todo.getUuidString());
        startActivityForResult(i, EDIT_ACTIVITY_CODE);*/
        Toast.makeText(this,todo.getTitle(),Toast.LENGTH_LONG);
    }

    private void loadFromParse() {
        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Todo");
        //query.findInBackground(new FindCallback<Todo>() {

        //ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Todo");

       // final ParseACL roleACL = new ParseACL();
       // roleACL.setPublicReadAccess(true);
       // final ParseRole role = new ParseRole("Engineer", roleACL);

     /*   if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseQuery<Todo> query = Todo.getQuery();
            //query.fromPin(TODO_GROUP_NAME);
            query.whereEqualTo("isDraft", false);

            try {
                List<Todo> objects = query.find();
                for (Todo t: objects)
                {
                    String aux = t.getTitle();
                }
                Log.d("Areas", "Areas " + objects.size() + " Areas");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } */

        todoListAdapter.clear();

        ParseQuery<Todo> query = Todo.getQuery();

        //query.whereEqualTo("author", "lVu9hZ77L3");//ParseUser.getCurrentUser());
        //query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        //query.include("owner");

        query.findInBackground(new FindCallback<Todo>() {
            @Override
             public void done(final List<Todo> todos, ParseException e) {

                if (e == null) {
                    ParseObject.pinAllInBackground((List<Todo>) todos,
                            new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        if (!isFinishing()) {

                                            for (Todo t: todos)
                                            {
                                                String aux = t.getTitle();
                                                Log.i("aux","aux: "+ aux +todos.size());
                                            }

                                            todoListAdapter.loadObjects();
                                           // todoListAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        Log.i("TodoListActivity",
                                                "Error pinning todos: "
                                                        + e.getMessage());
                                    }
                                }
                            });
                } else {
                    Log.i("TodoListActivity",
                            "loadFromParse: Error finding pinned todos: "
                                    + e.getMessage());
                }
            }
        });
    }

    private void syncTodosToParse() {
        // We could use saveEventually here, but we want to have some UI
        // around whether or not the draft has been saved to Parse
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni != null) && (ni.isConnected())) {
            if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                // If we have a network connection and a current logged in user,
                // sync the
                // todos

                // In this app, local changes should overwrite content on the
                // server.

                ParseQuery<Todo> query = Todo.getQuery();
                query.fromPin(TODO_GROUP_NAME);
                query.whereEqualTo("isDraft", true);
                query.findInBackground(new FindCallback<Todo>() {
                    public void done(List<Todo> todos, ParseException e) {
                        if (e == null) {
                            for (final Todo todo : todos) {
                                // Set is draft flag to false before
                                // syncing to Parse
                                todo.setDraft(false);
                                todo.saveInBackground(new SaveCallback() {

                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            // Let adapter know to update view
                                            if (!isFinishing()) {
                                                todoListAdapter
                                                        .notifyDataSetChanged();
                                            }
                                        } else {
                                            // Reset the is draft flag locally
                                            // to true
                                            todo.setDraft(true);
                                        }
                                    }

                                });

                            }
                        } else {
                            Log.i("TodoListActivity",
                                    "syncTodosToParse: Error finding pinned todos: "
                                            + e.getMessage());
                        }
                    }
                });
            } else {
                // If we have a network connection but no logged in user, direct
                // the person to log in or sign up.
               // ParseLoginBuilder builder = new ParseLoginBuilder(this);
               // startActivityForResult(builder.build(), LOGIN_ACTIVITY_CODE);
            }
        } else {
            // If there is no connection, let the user know the sync didn't
            // happen
            Toast.makeText(
                    getApplicationContext(),
                    "Your device appears to be offline. Some todos may not have been synced to Parse.",
                    Toast.LENGTH_LONG).show();
        }

    }

    private class ToDoListAdapter extends ParseQueryAdapter<Todo> {

        public ToDoListAdapter(Context context,
                               ParseQueryAdapter.QueryFactory<Todo> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(Todo todo, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.area_row, parent, false);
                holder = new ViewHolder();
                holder.todoTitle = (TextView) view
                        .findViewById(R.id.textViewNombre);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            TextView todoTitle = holder.todoTitle;
            todoTitle.setText(todo.getTitle());
            if (todo.isDraft()) {
                todoTitle.setTypeface(null, Typeface.ITALIC);
            } else {
                todoTitle.setTypeface(null, Typeface.NORMAL);
            }
            return view;
        }
    }

    private static class ViewHolder {
        TextView todoTitle;
    }

   /* public List<AreaInfo> getDataForListView()
    {
        List<AreaInfo> areaList = new ArrayList<AreaInfo>();

        for(int i=0;i<10;i++)
        {

            AreaInfo area = new AreaInfo();
            area.id = i;
            area.name = "Repsol";
            area.gas_station = 1;
            area.garage = 1;
            area.restaurant = 1;
            area.situation = 0;
            //area.position.latitude = 20.0;

            areaList.add(area);
        }

        return areaList;
    }*/
}
