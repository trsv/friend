package trsv.friend.data;

public class Profile {
	public boolean visible;
	public boolean anonimus;
	public boolean canInbox;
	public boolean privatemsg;
	public String lastOnline;

	public Profile() {
		visible = true;
		anonimus = false;
		canInbox = true;
		privatemsg = true;
		lastOnline = "";
	}
}