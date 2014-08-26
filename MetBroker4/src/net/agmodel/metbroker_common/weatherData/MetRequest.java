package net.agmodel.metbroker_common.weatherData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import net.agmodel.metbroker_common.genericBroker.ServerRequest;
import net.agmodel.metbroker_common.physical.Duration;
import net.agmodel.metbroker_common.physical.DurationUnit;
import net.agmodel.metbroker_common.physical.Interval;

/**
* Classes descending from MetRequest encapsulate all the details required in a request for meteorological data.<br>
* MetRequest is an abstract class which manages attributes which are common to all requests.<br>
* Descendant objects differ mainly in how they specify <u>where</u> the data is to come from.<br>
* {@link StationMetRequest} requests data from a specified station.<br>
* {@link SpatialMetRequest} requests data from all stations within an area.<br><br>
* <H3>Interpretation of dateExtremes in queries</H3>
* For point measurement data, MetBroker returns any data in the partially closed interval (start,end]<br>
* If the data is measured over periods of time, rather than at discrete points,
* MetBroker returns the data of any interval whose <b>endpoint</b>
* falls in the partially closed interval (start,end].<br><br>
* In general, client applications will only use the MetRequest constructors.<br>
* The remaining methods are primarily for MetBroker's internal use.
*
* @author Matthew Laurenson
*/
abstract public class MetRequest implements ServerRequest {
  private Interval dateExtremes;
  private MetDuration resolution;
  private boolean summarise;
  private boolean interpolate;
  private boolean[] requested;
  private long processingCommenced;
  private Locale locale;


  /**
   * localeを返す。
   * @return locale
   */
  public Locale getLocale() {
	return locale;
  }

  /**
   * localeをセットする。
   * @param locale
   */
  public void setLocale(Locale locale) {
	this.locale = locale;
  }

/**
  * Creates a request object
  * @param dateExtremes the interval for which data is requested (see note in class description)
  * @param requestedElements the set of elements requested
  * @param resolution the temporal resolution. See {@link net.agmodel.metbroker_commons.weatherData.MetDuration weatherData.MetDuration} for some useful constants.
  * @param summarise true if you want data from higher resolutions summarised to the target resolution.
  * @param interpolate true if you want MetBroker to find replacement values for missing data by spatial or temporal interpolation.
  * @param locale language locale.
  * @author Matthew Laurenson
  */
  protected MetRequest(Interval dateExtremes, Set requestedElements,
         MetDuration resolution,boolean summarise, boolean interpolate, Locale locale){
    this.dateExtremes=dateExtremes;
    this.resolution=resolution;
    this.summarise=summarise;
    this.interpolate=interpolate;
    this.locale = locale;
    requested = new boolean[MetElement.size()];
    Arrays.fill(requested,false);
    Iterator i=requestedElements.iterator();
    while (i.hasNext())
      requested[((MetElement) i.next()).ord]=true;
  }
  
  /**
   * Creates a request object not locale.
   * locale set default.
   * @param dateExtremes the interval for which data is requested (see note in class description)
   * @param requestedElements the set of elements requested
   * @param resolution the temporal resolution. See {@link net.agmodel.metbroker_commons.weatherData.MetDuration weatherData.MetDuration} for some useful constants.
   * @param summarise true if you want data from higher resolutions summarised to the target resolution.
   * @param interpolate true if you want MetBroker to find replacement values for missing data by spatial or temporal interpolation.
   * @author Matthew Laurenson
   */
   protected MetRequest(Interval dateExtremes, Set requestedElements,
          MetDuration resolution,boolean summarise, boolean interpolate){
     this.dateExtremes=dateExtremes;
     this.resolution=resolution;
     this.summarise=summarise;
     this.interpolate=interpolate;
     this.locale = Locale.getDefault();
     requested = new boolean[MetElement.size()];
     Arrays.fill(requested,false);
     Iterator i=requestedElements.iterator();
     while (i.hasNext())
       requested[((MetElement) i.next()).ord]=true;
   }

  /**
  * Creates a request object allowing summarising but not interpolation
  * @param dateExtremes the interval for which data is requested (see note in class description)
  * @param requestedElements the set of elements requested
  * @param resolution the temporal resolution. See {@link net.agmodel.metbroker_commons.weatherData.MetDuration weatherData.MetDuration} for some useful constants.
  */
  protected MetRequest(Interval dateExtremes, Set requestedElements,
         MetDuration resolution) {
    this(dateExtremes,requestedElements,resolution,true,false, Locale.getDefault());
  }
  
  protected MetRequest(Interval dateExtremes, Set requestedElements,
	         MetDuration resolution,Locale locale) {
	    this(dateExtremes,requestedElements,resolution,true,false, locale);
	  }

  /**
  * Creates a request object for daily data allowing summarising but not interpolation
  * @param dateExtremes the interval for which data is requested (see note in class description)
  * @param requestedElements the set of elements requested
  */
  protected MetRequest(Interval dateExtremes, Set requestedElements) {
    this(dateExtremes, requestedElements,MetDuration.DAILY,true,false, Locale.getDefault());
  }

  /**
  * Creates a request object for daily data allowing summarising but not interpolation
  * @param dateExtremes the interval for which data is requested (see note in class description)
  * @param requestedElements the set of elements requested
  */
  protected MetRequest(Interval dateExtremes, Set requestedElements, Locale locale) {
    this(dateExtremes, requestedElements,MetDuration.DAILY,true,false, locale);
  }

  /**
  * Indicates whether a meteorological element is included in a request
  * @param aMetElement the element of interest
  * @return true if the request includes the element, false otherwise
  */
  public boolean containsMetElement(MetElement aMetElement) {
    return requested[aMetElement.ord];
  }

  /**
  * Gets the interval for which data is requested.
  * @param aMetElement the element of interest
  * @return the request interval
  */
  public Interval getDateExtremes () {
    return dateExtremes;
  }

  /**
  * Gets all the meteorological elements requested.
  * @return an array of requested elements
  */
  public MetElement[] getRequested() {
    Set result=new HashSet(MetElement.size());
    MetElement m=MetElement.first();
    for (int i=0;i<requested.length;i++) {
      if (requested[i])
        result.add(m);
      m=m.next();
    }
    return (MetElement[]) result.toArray(new MetElement[0]);
  }

  /**
  * Gets the requested resolution for the result of the query.
  * If summarising or interpolating are enabled,
  * data having other resolutions may be processed to this resolution
  *
  * @return the resolution
  */
  public MetDuration getResolution() {
    return resolution;
  }

  /**
  * Sets the time that processing commenced using a timing source like System.currentTimeMillis()<br>
  * Called by MetBroker when it receives a query.<br>
  * Normally client applications would not use this.
  */
  public void setProcessingCommenced() {
    processingCommenced=System.currentTimeMillis();
  }

  /**
  * Returns the elapsed time since the last call to setProcessingCommenced.<br>
  * Called by MetBroker just before it returns the query results.<br>
  * Normally client applications would use {@link StationDataSet#getServerProcessingTime()}.
  *
  * @return the elapsed duration
  */
  public Duration getElapsedTime() {
    return new Duration(System.currentTimeMillis()-processingCommenced, DurationUnit.MILLISECOND);
  }

  /**
  * Gets whether the query requests summarisation.<br>
  *
  * @return true if the query requests that data be summarised, false otherwise.
  */
  public boolean shouldSummarise() {
    return summarise;
  }

  /**
  * Gets whether the query requests interpolation.<br>
  *
  * @return true if the query requests that data be interpolated, false otherwise.
  */
  public boolean shouldInterpolate() {
    return interpolate;
  }
  /**
  * Sets whether the query requests summarisation.<br>
  *
  * @param summarise true to request that data be summarised, false otherwise.
  */
  protected void setSummarise(boolean summarise) {
    this.summarise=summarise;
  }

  /**
  * Sets whether the query requests interpolation.<br>
  *
  * @param interpolate true to request that data be interpolated, false otherwise.
  */
  protected void setInterpolate(boolean interpolate) {
    this.interpolate=interpolate;
  }

  /**
  * Get a string representation of the query parameters
  * @return a string in a mixture of English and the default language;
  */
  public String toString() {
    StringBuffer output=new StringBuffer(50);
    output.append("\n"+getClass().getName()+"\n");
    output.append("\t"+this.getDateExtremes()+"\n\telements");
    MetElement element=MetElement.first();
    for (int i=0;i<requested.length;i++) {
      if (requested[i])
        output.append("\t"+element);
      element=element.next();
    }
    output.append("\n\tresolution\t"+getResolution()+"\n");
    output.append("\tSummarise\t"+summarise+"\t");
    output.append("Interpolate\t"+interpolate+"\n");
    return output.toString();
  }
}
