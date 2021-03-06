/*
 * MIT License
 *
 * Copyright (c) 2020 CharlieCondorcet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cl.ucn.disc.dsm.charlie.p2pchat.room.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import cl.ucn.disc.dsm.charlie.p2pchat.entities.ChatUser;
import cl.ucn.disc.dsm.charlie.p2pchat.entities.Conversation;
import cl.ucn.disc.dsm.charlie.p2pchat.entities.Message;
import cl.ucn.disc.dsm.charlie.p2pchat.room.ChatUserDao;
import cl.ucn.disc.dsm.charlie.p2pchat.room.ConversationDao;
import cl.ucn.disc.dsm.charlie.p2pchat.room.MessageDao;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The RoomDataBase class to instance the data base to the complete Room.
 *
 * @author Charlie Condorcet.
 */
//TODO: change the exportSchema to listen the Backend.
@Database(entities = {Message.class, ChatUser.class,
    Conversation.class}, version = 5, exportSchema = false)
public abstract class ProyectRoomDatabase extends RoomDatabase {

  // Dao instance to Message.
  public abstract MessageDao messageDao();

  // Dao instance to ChatUser.
  public abstract ChatUserDao chatUserDao();

  // Dao instance to Conversation.
  public abstract ConversationDao conversationDao();

  // Instance to user singleton.
  private static volatile ProyectRoomDatabase INSTANCE;

  // The executor.
  private static final int NUMBER_OF_THREADS = 4;
  static final ExecutorService databaseWriteExecutor =
      Executors.newFixedThreadPool(NUMBER_OF_THREADS);

  // Create the database to Room.
  static ProyectRoomDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (ProyectRoomDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              ProyectRoomDatabase.class, "proyect_database")
              .addCallback(sRoomDatabaseCallback)
              // Add a migration delete to start a new migration with a new DB.
              .fallbackToDestructiveMigration()
              .build();
        }
      }
    }
    return INSTANCE;
  }

  // Method to poblate the data base with a examples to print in the Activity.
  private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
      super.onOpen(db);

      // If you want to keep data through app restarts,
      // comment out the following block
      databaseWriteExecutor.execute(() -> {
        // Populate the database in the background.
        // If you want to start with more messages, just add them.
        MessageDao dao = INSTANCE.messageDao();
        dao.deleteAll();

        Date date =new Date();
        Message message = new Message( "Hey there!", date);
        dao.insert(message);
        message = new Message( "I am using CatChat.", date);
        dao.insert(message);

        // Use the order from the corresponding table.
        ChatUserDao chatUserDao = INSTANCE.chatUserDao();
        chatUserDao.deleteAll();

        // Add 2 users to example.
        ChatUser user = new ChatUser("tommy", "tommy99@gmail.com", "HolaHola123");
        chatUserDao.insert(user);
        user = new ChatUser("camila", "camiflower31@aol.com", "12345678abC");
        chatUserDao.insert(user);

      });
    }
  };

}
