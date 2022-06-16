/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.basic_innovations.kahiyeapp;

public class ChatMessage  {
    private String recieverID;
    private String text;
    private String name;
    private String photoUrl;
    public ChatMessage() {
    }

    public ChatMessage(String text, String name, String photoUrl, String recieverID) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.recieverID = recieverID;
    }



    /*public static final Creator<FriendlyMessage> CREATOR = new Creator<FriendlyMessage>() {
        @Override
        public FriendlyMessage createFromParcel(Parcel in) {
            return new FriendlyMessage(in);
        }

        @Override
        public FriendlyMessage[] newArray(int size) {
            return new FriendlyMessage[size];
        }
    };
*/
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRecieverID() {
        return recieverID;
    }

    public void setRecieverID(String recieverID) {
        this.recieverID = recieverID;
    }

}
