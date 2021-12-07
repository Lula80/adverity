API calculates 3 types of metrics
1. aggregated metric, i.e.:
{
  "aggregator": "AVG",
  "campaign": "Adventmarkt Touristik",
  "daily": {
    "from": "11/01/19",
    "to": "11/30/19"
  },
  "dataSource": "Google Ads",
  "metric": "CLICKS"
}
 - aggregator function for SQL GROUP BY operator
 -  optional list of dimensions (campaign, daily, daaSource) for WHERE operator
 Returns result of applying aggregation function over metric "Impressions".
 
 2. Group requests to calculate metrics grouped by se of dimensions, applied common grouping
 functions MIN, MAX, COUNT,SUN, AVG. Aggregator and metric are required parameters. 
    {
      "aggregator": "SUM",
      "dimensions": [
        "Datasource", "Daily"
      ],
      "having": {
        "maximum": 1000,
        "minimum": 90
      },
      "metric": "CLICKS"
    }
 3. Delivering as result simple list of passed metric values, obtained by applying passed 
 dimensions as filters. Only metric parameer is necessary.
 {
   "campaign": "Adventmarkt Touristik",
   "daily": {
     "from": "11/01/19",
     "to": "11/30/19"
   },
   "dataSource": "Google Ads",
   "metric": "CLICKS"
 }