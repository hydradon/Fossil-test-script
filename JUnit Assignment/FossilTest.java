import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FossilTest {

	WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void testAddNewTicket() {
		driver.get("http://127.0.0.1:8080/");

		driver.findElement(By.linkText("Tickets")).click();
		driver.findElement(By.linkText("New")).click();

		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("testTicket3");
		driver.findElement(By.name("icomment")).sendKeys(
				"test defects a b c d e");
		driver.findElement(By.name("preview")).click();
		driver.findElement(By.name("submit")).click();
		List<WebElement> eles = driver
				.findElements(By.className("tktDspValue"));
		assertEquals(eles.get(1).getText(), "testTicket3");
		assertEquals(driver.findElement(By.className("verbatim")).getText(),
				"test defects a b c d e");
	}

	@Test
	public void testAddAndEditTicket() {

		// Adding a new ticket
		driver.get("http://127.0.0.1:8080/");
		driver.findElement(By.linkText("Tickets")).click();
		driver.findElement(By.linkText("New")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Ticket to be edited");
		driver.findElement(By.name("icomment")).sendKeys(
				"Description of this ticket to be edited by JUnit");
		driver.findElement(By.name("preview")).click();
		driver.findElement(By.name("submit")).click();

		// Editing the ticket
		driver.findElement(By.linkText("Edit")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title"))
				.sendKeys("Ticket has been edited!");
		driver.findElement(By.name("preview")).click();
		driver.findElement(By.name("submit")).click();
		List<WebElement> eles = driver
				.findElements(By.className("tktDspValue"));
		assertNotEquals(eles.get(1).getText(), "Ticket to be edited");
	}

	@Test
	public void testAddNewReportFormat() {
		driver.get("http://127.0.0.1:8080/rptnew");
		driver.findElement(By.name("t")).sendKeys(
				"Add New Report Format " + System.currentTimeMillis());
		driver.findElement(By.name("t")).submit();
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 7),
				"rptview");
	}

	@Test
	public void testAddDuplicateNewReportFormat() {
		driver.get("http://127.0.0.1:8080/rptnew");
		driver.findElement(By.name("t")).sendKeys("All tickets report");
		driver.findElement(By.name("t")).submit();
		assertEquals(driver.findElement(By.className("reportError")).getText(),
				"There is already another report named \"All tickets report\"");
	}

	@Test
	public void testAddAndDeleteReportFormat() {
		driver.get("http://127.0.0.1:8080/rptnew");
		driver.findElement(By.name("t")).sendKeys("Report to be deleted");
		driver.findElement(By.name("t")).submit();
		driver.get("http://127.0.0.1:8080/rptedit?rn="
				+ driver.getCurrentUrl().substring(
						driver.getCurrentUrl().indexOf('=') + 1));
		driver.findElement(By.name("del1")).click();
		driver.findElement(By.name("del2")).click();
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1), "reportlist");
	}

	@Test
	public void CreateWiki() {
		//t = new Tracer();
		String wikiTitle = "wiki test";
		//driver = t.SetProfile();
		driver.get("http://localhost:8080/index/");
		driver.findElement(By.linkText("Wiki")).click();
		driver.findElement(By.linkText("new wiki page")).click();
		WebElement query = driver.findElement(By.name("name"));
		query.sendKeys(wikiTitle);
		driver.findElement(
				By.cssSelector("input[value='Create'][type='submit']")).click();
		WebElement wikiContent = driver.findElement(By.name("w"));
		wikiContent.clear();
		wikiContent.sendKeys("This is a test on the wiki creation function!!");
		driver.findElement(
				By.cssSelector("input[value='Apply These Changes'][type='submit']"))
				.click();

		if (driver.findElement(By.tagName("title")).getText().toLowerCase()
				.contains(wikiTitle))
			assertEquals(driver.findElements(By.tagName("p")).get(0).getText(),
					"This is a test on the wiki creation function!!");

		driver.close();

		driver.quit();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void ListAllWiki() {
		//t = new Tracer();
		//driver = t.SetProfile();
		driver.get("http://localhost:8080/index/");
		String[] wikiList = { "wiki test" };
		driver.findElement(By.linkText("Wiki")).click();
		driver.findElement(By.linkText("List of All Wiki Pages")).click();
		String wikinames = driver.findElements(By.tagName("ul")).get(0)
				.getText();
		String[] pagewikis = wikinames.split("\\n");
		assertEquals(pagewikis, wikiList);
		driver.close();
		driver.quit();
	}

	@Test
	public void AppendToWiki() {
		//t = new Tracer();
		String wikiTitle = "wiki test";
		//driver = t.SetProfile();
		driver.get("http://localhost:8080/index/");
		driver.findElement(By.linkText("Wiki")).click();
		driver.findElement(By.linkText("new wiki page")).click();
		WebElement query = driver.findElement(By.name("name"));
		query.sendKeys(wikiTitle);
		driver.findElement(
				By.cssSelector("input[value='Create'][type='submit']")).click();
		WebElement wikiContent = driver.findElement(By.name("w"));
		wikiContent.clear();
		wikiContent.sendKeys("This is a test on the wiki creation function!!");
		driver.findElement(
				By.cssSelector("input[value='Apply These Changes'][type='submit']"))
				.click();
		driver.findElement(By.linkText("Append")).click();
		WebElement commentArea = driver.findElement(By.name("r"));
		commentArea.sendKeys("test on comment");

		driver.findElement(
				By.cssSelector("input[value='Append Your Changes'][type='submit']"))
				.click();
		if (driver.getPageSource().contains("test on comment"))
			System.out.println("true");
		else
			System.out.println("false");

	}

}
