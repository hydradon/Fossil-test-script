import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MyAdapter {

	WebDriver driver;

	public MyAdapter() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get("http://127.0.0.1:8080/");
	}
	
/*-----------------------Reset-------------------------------------- */
	public void reset() {
		driver.quit();
		driver = new FirefoxDriver();
		driver.get("http://127.0.0.1:8080/");
	}

/*-----------------------Home-------------------------------------- */
	public void clickHome() {
		driver.findElement(By.linkText("Home")).click();
	}
	
	
/*-----------------------Ticket System----------------------------- */
	public void clickTicketsPage() {
		driver.findElement(By.linkText("Tickets")).click();
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 6), "ticket");
	}

	public void clickNewTicket() {
		driver.findElement(By.linkText("New")).click();
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 6), "tktnew");
	}

	public void enterTestTicketDetails() {
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("testTicket3");
		driver.findElement(By.name("icomment")).sendKeys("test defects a b c d e");
	}

	public void enterEditedTicketDetails() {
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Ticket has been edited!");
	}

	// returns ID of updated Ticket
	public String clickSubmitTicket() {
		driver.findElement(By.name("preview")).click();
		driver.findElement(By.name("submit")).click();

		List<WebElement> eles = driver.findElements(By.className("tktDspValue"));
		assertEquals(eles.get(1).getText(),"testTicket3");
		assertEquals(driver.findElement(By.className("verbatim")).getText(), "test defects a b c d e");
		return driver.getCurrentUrl().substring(driver.getCurrentUrl().indexOf('=') + 1);
	}

	// returns ID of updated Ticket
	public String clickSubmitEditedTicket() {
		driver.findElement(By.name("preview")).click();
		driver.findElement(By.name("submit")).click();
		
		List<WebElement> eles = driver.findElements(By.className("tktDspValue"));
		assertEquals(eles.get(1).getText(), "Ticket has been edited!");
		return driver.getCurrentUrl().substring(driver.getCurrentUrl().indexOf('=') + 1);
	}

	public void editTicket() {
		driver.findElement(By.linkText("Edit")).click();
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 7), "tktedit");
	}

	/*public void clickViewTicket() {
		
	}*/
	
/*-----------------------Report System------------------------------ */
	public void clickNewReport() {
		driver.findElement(By.linkText("New report format")).click();
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 6), "rptnew");
	}

	public void enterReportDetails(int reportNameSuffix) {
		driver.findElement(By.name("t")).clear();
		driver.findElement(By.name("t")).sendKeys("testReport" + String.valueOf(reportNameSuffix));
	}

	public String clickSubmitReport() {
		driver.findElement(By.cssSelector("input[value='Apply Changes'][type='submit']")).click();
		
		if (driver.findElements(By.tagName("blockquote")).size() != 0) { //Check if a report already exists
			return "-1";
		} else {
			String URL = driver.getCurrentUrl();
			assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 7), "rptview");
			
			return driver.getCurrentUrl().substring(driver.getCurrentUrl().indexOf('=') + 1);
		}	
	}

	public String viewReport() {
		List<WebElement> reportList = driver.findElement(By.tagName("ol")).findElements(By.tagName("li"));
		assertNotEquals(reportList.size(), 0);

		reportList.get(randInt(0, reportList.size()-1))  //choose a random report from list
					.findElement(By.tagName("a")).click();
		
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 7), "rptview");
		
		return URL.substring(URL.indexOf('=') + 1);
	}
	
	public void editReport() {
		driver.findElements(By.className("label")).get(0).click();
		
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 7), "rptedit");
	}

	public String deleteReport() {
		String deletedReportID = driver.getCurrentUrl().substring(driver.getCurrentUrl().indexOf('=') + 1);
		
		driver.findElement(By.name("del1")).click(); //click Delete this report in the report Edit page
		String URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 7), "rptedit");
		
		driver.findElement(By.name("del2")).click(); //click Delete this report in the "Are u sure?" page
		URL = driver.getCurrentUrl();
		assertEquals(URL.substring(URL.indexOf("/", 8) + 1).substring(0, 10), "reportlist");
		
		return deletedReportID;
	}	
	
	//delete all reports function used when reset
	public void deleteAllReport() {
		clickTicketsPage();
		int nbReports = driver.findElement(By.tagName("ol")).findElements(By.tagName("li")).size();
		
		while (nbReports > 0){
			viewReport();	
			editReport();
			deleteReport();
			nbReports--;
		}
	}

	public int getNumberOfReports() {
		clickTicketsPage();
		return driver.findElement(By.tagName("ol")).findElements(By.tagName("li")).size();
	}
	
	public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	/*------------------------Wiki System-------------------*/
	
	public void WikiHomeLink()
    {
		driver.findElement(By.linkText("Wiki")).click();
    	assert
    	driver.getPageSource().contains("Wiki Links");
    }
    public void NewWiki() {
    	driver.findElement(By.linkText("new wiki page")).click();
        assert
        driver.getPageSource().contains("Create A New Wiki Page");
      }
    public void CreateWiki() {
    	driver.findElement(By.name("name")).sendKeys("ModelTest");
    	driver.findElement(By.cssSelector("input[value='Create'][type='submit']")).click();
        assert 
        driver.getPageSource().contains("Edit: ModelTest");
      }
   public void Submit()
   {
	   WebElement wikiContent = driver.findElement(By.name("w")); // w is the name given to the textarea element.
       wikiContent.clear();
       wikiContent.sendKeys("test on the wiki creation function");
       
       driver.findElement(By.cssSelector("input[value='Apply These Changes'][type='submit']")).click();
	   
       if(driver.findElement(By.tagName("title")).getText().toLowerCase().contains("ModelTest"))
           assert
           driver.findElements(By.tagName("p")).get(0).getText() == "test on the wiki creation function";
         
   }
//   public void AppendCommentLink() {
//       
//       assert 
//       mDriver.getPageSource().contains("Edit: ModelTest");
//     }
   public void AppendComment()
   {
	   driver.findElement(By.linkText("Append")).click();
	   WebElement wikiContent = driver.findElement(By.name("r")); // r is the name given to the textarea element for comments insertion.
       wikiContent.sendKeys("Comment on Wiki");
       driver.findElement(By.cssSelector("input[value='Append Your Changes'][type='submit']")).click();
       assert
       driver.getPageSource().contains("Comment on Wiki");
     
   }
   
   public void ListAllWikiPages() {
	   driver.findElement(By.linkText("List of All Wiki Pages")).click();
       assert 
       driver.getPageSource().contains("Available Wiki Pages");
     }
   
   public void ShowOverview()
   {
	   driver.findElement(By.linkText("Details")).click(); 
	   assert
	   driver.getPageSource().contains("Overview");
	   
	  
   }
   public void SelectWiki()
   {
	   driver.findElement(By.linkText("ModelTest")).click();
	   assert 
	   driver.getPageSource().contains("ModelTest");
   }
   
  /* public void AttachLink()
   {
	   mDriver.findElement(By.linkText("Attach")).click();
	   assert 
       mDriver.getPageSource().contains("Add Attachment");
   }*/
   public void AttachFile()
   {
	   //
	   driver.findElement(By.linkText("Attach")).click();
	   WebElement browseBtn = driver.findElement(By.name("f"));
	   File file = new File("testtextfile.txt");
	   browseBtn.sendKeys(file.getAbsolutePath());
	   try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	  // mDriver.findElement(By.name("f")).sendKeys("Agile testing terminology.pdf"); // a pdf file 
	   driver.findElement(By.name("ok")).click();
	   assert
	   driver.getPageSource().contains("Agile testing terminology");
   }
	
}