package luc.hci.emotext.interfaces;

import luc.hci.emotext.info_type.InfoOfFriend;
import luc.hci.emotext.info_type.InfoOfMessage;

public interface Updater
{

	public void updateData( InfoOfMessage[ ] messages, InfoOfFriend[ ] friends, InfoOfFriend[ ] unApprovedFriends, String userKey );

}