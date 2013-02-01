<?php
	$symbol= $_GET["symbol"];
	$file=fopen("http://finance.yahoo.com/d/quotes.csv?s=EBAY+STX+DELL+BRCM+NVDA+SNDK+WIN+HPQ+ZNGA+SWKS+EA+CTXS+ORCL+FB+YHOO+MSFT+AAPL+GOOG+AMZN+CSCO+RIMM+QCOM+INTC&f=sj1","r")
	or exit("Unable to open file!");
	while(!feof($file)) {
		$value = fgets($file);
		$va1 = str_replace("\"", "", $value);
		$va2 = str_replace("B", "", $va1);
		$va3 = str_replace(",", "\n", $va2);
		$va4 = str_replace(" ", "", $va3);
		echo $va4;
	}
	fclose($file);
?>