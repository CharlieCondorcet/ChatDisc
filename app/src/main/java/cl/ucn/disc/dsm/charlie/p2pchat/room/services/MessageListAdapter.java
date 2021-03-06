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

package cl.ucn.disc.dsm.charlie.p2pchat.room.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.charlie.p2pchat.R;
import cl.ucn.disc.dsm.charlie.p2pchat.entities.Message;
import java.util.List;

/**
 * The List Adapter to show data in the Recycler View.
 *
 * @author Charlie Condorcet.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>{

  // Find the Message received in the activity.
  class MessageViewHolder extends RecyclerView.ViewHolder {
    private final TextView messageItemView;

    private MessageViewHolder(View itemView) {
      super(itemView);
      messageItemView = itemView.findViewById(R.id.tv_t_new_message_message);
    }
  }

  private final LayoutInflater mInflater;

  // Cached copy of Messages.
  private List<Message> mMessages;

  // The constructor to MessageListAdapter.
  public MessageListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

  @Override
  public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
    return new MessageViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(MessageViewHolder holder, int position) {
    if (mMessages != null) {
      Message current = mMessages.get(position);
      holder.messageItemView.setText(current.getText());
    } else {
      // Covers the case of data not being ready yet.
      holder.messageItemView.setText("No Message");
    }
  }

  public void setMessages(List<Message> messages){
    mMessages = messages;
    notifyDataSetChanged();
  }

  // getItemCount() is called many times, and when it is first called,
  // mMessages has not been updated (means initially, it's null, and we can't return null).
  @Override
  public int getItemCount() {
    if (mMessages != null)
      return mMessages.size();
    else return 0;
  }

}
