// This class responsible to start thread of acceptors

public class MemberRunnable {
    private Member member;

    public MemberRunnable(Member member) {
        this.member = member;
        new ServerThread(member).start();
    }

    public void propose() {
        ((Member) member).propose();
    }
}
