package utilities;

/*
 * Class:		DateUtilities
 * Description:	Adds new features for the DateTime Class
 * 				Edited as necessary by studnet being assessed
 
 * Author:		Rodney Cocker
 * 				Edited: Nicholas Balliro - s3541341
 */
public class DateUtilities {

	public static boolean dateIsNotInPast(DateTime date)
	{
		final int OFFSET_FOR_DAYS_IN_MILLISECONDS = 1;
		boolean notInPast = false;
		
		DateTime today = new DateTime();
		
		int daysInPast = DateTime.diffDays(date, today) + OFFSET_FOR_DAYS_IN_MILLISECONDS;
		if(daysInPast >=0)
		{
			notInPast = true;
		}
		
		return notInPast;
	}
	
	public static boolean datesAreTheSame(DateTime date1, DateTime date2)
	{
		if(date1.getEightDigitDate().equals(date2.getEightDigitDate()))
		{
			return true;
		}
		return false;
	}
	
	//NOTE: Adjusted this method to be able to take different inputs
	public static boolean dateIsNotMoreThanXDays(DateTime date, int x)
	{
		
		boolean withinXDays = false;
		DateTime today = new DateTime();
		DateTime nextWeek = new DateTime(x);
		
		int daysInFuture = DateTime.diffDays(nextWeek, date);
		if(daysInFuture >0 && daysInFuture <(x+1))
		{
			withinXDays = true;
		}
		return withinXDays;
	}
	
}
