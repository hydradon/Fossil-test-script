import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.AllRoundTester;
import nz.ac.waikato.modeljunit.FsmModel;
import nz.ac.waikato.modeljunit.GreedyTester;
import nz.ac.waikato.modeljunit.LookaheadTester;
import nz.ac.waikato.modeljunit.RandomTester;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.VerboseListener;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import nz.ac.waikato.modeljunit.gui.visualisaton.VisualisationListener;
import nz.ac.waikato.modeljunit.StopOnFailureListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ModelFossil implements FsmModel {

	public enum Page {
		Home, TicketsMainMenu, NewTicket, ViewTicket, EditTicket, NewReport, ViewReport, EditReport, WikiHome, WikiName, WikiBody, WikiEdit, WikiComment, WikAttachment, WikiOverview, WikiList
	}

	public enum WikiStatus {
		Created, Open, Initial, Submitted
	}

	public enum FileStatus {
		Appended, Uncreated
	}

	private Page mPage = Page.Home;
	private int nbTickets = 0;
	private WikiStatus wStatus = WikiStatus.Initial;
	private FileStatus fStatus = FileStatus.Uncreated;
	private int nbFiles, nbComments, nbWikis = 0;

	private String currentTicketID = "";
	private String currentReportID = "";

	private MyAdapter mAdapter = new MyAdapter();
	// private int nbReports = mAdapter.getNumberOfReports();
	private int nbReports = 0;

	/*-----------------------Home-------------------------------------- */
	@Action
	public void clickHome() {
		mAdapter.clickHome();
		mPage = Page.Home;
	}

	public boolean clickHomeGuard() {
		return mPage != Page.Home && (mPage == Page.TicketsMainMenu || mPage == Page.WikiHome);
	}

	/*-----------------------Ticket System------------------------------ */
	@Action
	public void clickTicketsPage() {
		mAdapter.clickTicketsPage();
		mPage = Page.TicketsMainMenu;
	}

	public boolean clickTicketsPageGuard() {
		return mPage != Page.TicketsMainMenu && (mPage == Page.Home || mPage == Page.ViewReport || mPage == Page.ViewTicket);
	}

	@Action
	public void newTicket() {
		mAdapter.clickNewTicket();
		mPage = Page.NewTicket;
	}

	public boolean newTicketGuard() {
		return mPage == Page.TicketsMainMenu;
	}

	@Action
	public void submitTicket() {
		mAdapter.enterTestTicketDetails();
		currentTicketID = mAdapter.clickSubmitTicket();
		nbTickets++;
		mPage = Page.ViewTicket;
	}

	public boolean submitTicketGuard() {
		return mPage == Page.NewTicket;
	}

	@Action
	public void editTicket() {
		mAdapter.editTicket();
		mPage = Page.EditTicket;
	}

	public boolean editTicketGuard() {
		return mPage == Page.ViewTicket;
	}

	@Action
	public void submitEditedTicket() {
		mAdapter.enterEditedTicketDetails();
		currentTicketID = mAdapter.clickSubmitEditedTicket();
		mPage = Page.ViewTicket;
	}

	public boolean submitEditedTicketGuard() {
		return mPage == Page.EditTicket;
	}

	/*-----------------------Report System------------------------------ */
	@Action
	public void clickNewReport() {
		mAdapter.clickNewReport();
		mPage = Page.NewReport;
	}

	public boolean clickNewReportGuard() {
		return mPage == Page.TicketsMainMenu;
	}

	@Action
	public void viewReport() {
		//if (nbReports > 0) {
			currentReportID = mAdapter.viewReport();
			mPage = Page.ViewReport;
		//}
	}

	public boolean viewReportGuard() {
		return mPage == Page.TicketsMainMenu && nbReports > 0;
	}

	@Action
	public void editReport() {
		mAdapter.editReport();
		mPage = Page.EditReport;
	}

	public boolean editReportGuard() {
		return mPage == Page.ViewReport;
	}

	@Action
	public void submitReport() {
		currentReportID = "-1";

		if (mPage == Page.NewReport) {
			nbReports++;
		}

		int reportNameSuffix = nbReports;

		while (currentReportID == "-1") { // repeat until there's no more
											// duplicated name
			mAdapter.enterReportDetails(reportNameSuffix++);
			currentReportID = mAdapter.clickSubmitReport();
		}

		mPage = Page.ViewReport;
	}

	public boolean submitReportGuard() {
		return mPage == Page.EditReport || mPage == Page.NewReport;
	}

	@Action
	public void deleteReport() {
		currentReportID = mAdapter.deleteReport();
		mPage = Page.TicketsMainMenu;
		nbReports--;
	}

	public boolean deleteReportGuard() {
		return mPage == Page.EditReport;
	}

	/*-----------------------Wiki System----------------------------------*/

	@Action
	public void WikiHomeLink() { // The wikihomelink should be treated as the
									// logout
		wStatus = WikiStatus.Initial;
		mAdapter.WikiHomeLink();
		mPage = Page.WikiHome;
		
	}

	public boolean WikiHomeLinkGuard() {
		return mPage != Page.WikiHome && (mPage == Page.Home || mPage == Page.WikiList || mPage == Page.WikiBody || mPage == Page.WikiComment || mPage == Page.WikiOverview || mPage == Page.WikiName || mPage == Page.WikAttachment);
	}

	@Action
	public void ListWikis() {
		mAdapter.ListAllWikiPages();
		mPage = Page.WikiList;
		
	}

	public boolean ListWikisGuard() {
		return mPage == Page.WikiHome && wStatus == WikiStatus.Initial;
	}

	@Action
	public void NewWiki() { // The New wiki link is available from the WikiHome
							// page
		mAdapter.NewWiki();
		mPage = Page.WikiName;
	}

	public boolean NewWikiGuard() {
		return mPage == Page.WikiHome && wStatus == WikiStatus.Initial;
	}

	@Action
	public void CreateWiki() {
		mAdapter.CreateWiki();
		wStatus = WikiStatus.Created;
		mPage = Page.WikiBody;
	}

	public boolean CreateWikiGuard() {
		return mPage == Page.WikiName && wStatus == WikiStatus.Initial;
	}

	@Action
	public void Submit() // filling the wiki body and submiting the changes
	{
		mAdapter.Submit();
		wStatus = WikiStatus.Submitted;
		mPage = Page.WikiEdit;
		nbWikis++;
	}

	public boolean SubmitGuard() {
		return mPage == Page.WikiBody && wStatus == WikiStatus.Created;
	}

	@Action
	public void SelectWiki() {
		mAdapter.SelectWiki();
		wStatus = WikiStatus.Open;
		mPage = Page.WikiEdit;
	}

	public boolean SelectWikiGuard() {
		return mPage == Page.WikiList && wStatus == WikiStatus.Initial
				&& nbWikis > 0;
	}

	@Action
	public void AppendComment() {
		mAdapter.AppendComment();
		mPage = Page.WikiEdit;
		nbComments++;
	}

	public boolean AppendCommentGuard() {
		return mPage == Page.WikiEdit;
	}

	@Action
	public void AttachFile() {
		mAdapter.AttachFile();
		mPage = Page.WikAttachment;
		fStatus = FileStatus.Appended;
		nbFiles++;
	}

	public boolean AttachFileGuard() {
		return mPage == Page.WikiEdit && wStatus == WikiStatus.Open;// &&
																	// fStatus
																	// ==
																	// FileStatus.Uncreated;
	}

	@Action
	public void ShowFileOverView() {
		mAdapter.ShowOverview();
		mPage = Page.WikiOverview;
	}

	public boolean ShowFileOverViewGuard() {
		return mPage == Page.WikiEdit && wStatus == WikiStatus.Open
				&& nbFiles > 0;
	}

	/*-----------------------Get State------------------------------ */
	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		// return mPage + " " + nbTickets + " " + nbReports;
		if (mPage == Page.WikAttachment || 
				mPage == Page.WikiBody || 
				mPage == Page.WikiComment || 
				mPage == Page.WikiEdit || 
				mPage == Page.WikiHome || 
				mPage == Page.WikiList || 
				mPage == Page.WikiName ||
				mPage == Page.WikiOverview
				)
			return mPage + " " + wStatus;
		else
			return mPage;
	}

	/*-----------------------Reset---------------------------------- */
	@Override
	public void reset(boolean arg0) {
		// TODO Auto-generated method stub
		mAdapter.deleteAllReport();
		nbReports = 0;
		nbFiles = 0;
		wStatus = WikiStatus.Initial;
		//System.out.println("resettt");
		mAdapter.clickHome();
		mPage = Page.Home;
//		if (arg0)
//			mAdapter.reset();
	}

	/*-----------------------Main---------------------------------- */
	public static void main(String[] argv) throws FileNotFoundException {
		ModelFossil model = new ModelFossil();

		//Tester tester = new LookaheadTester(model);
		//Tester tester = new RandomTester(model);
		//Tester tester = new AllRoundTester(model);
		Tester tester = new GreedyTester(model);

		//tester.buildGraph().printGraphDot("Graph.dot");

		tester.addListener(new VerboseListener());
		tester.addListener(new StopOnFailureListener());

		tester.addCoverageMetric(new TransitionCoverage());
		tester.addCoverageMetric(new StateCoverage());
		tester.addCoverageMetric(new ActionCoverage());
		tester.addCoverageMetric(new TransitionPairCoverage());

		tester.generate(200);
		tester.printCoverage();
	}

}
