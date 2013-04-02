/*
to do
 - fix jitter when removing companies
 - fix blur around circles
 - add a flash when you shrink/grow?
 - fix first two companies showing up without circles
 - add title screen with instructions 
 - change difficulty based on screen size
 */

ArrayList companies = new ArrayList();

// myMan sizing
int es = 50;

//global variables for background color
color bgc = 30;

//gloabl variable for array of stocks and prices
String[] mCaps;

//gloabl win variable
boolean win = false;

// Timing variables
int startTime;
int counter = 0;
int once = 0;


void setup() {
	background(bgc)
  size(800, 600);
  startTime = millis();
  mCaps = loadStrings("all.php");
  // println(mCaps);
  noCursor();
  strokeWeight(1);
}

void draw() {
  stroke(255);
  scene();
  populateCompany();
  displayCompanies();
  myMan();
}

// Behavior and display for "myMan"
void myMan() {
  fill(225, 100, 100);
  ellipse(mouseX, mouseY, es, es);
  fill(255);
  textSize(int(es/5));
  text(str(int(es))+"M", mouseX, mouseY);
  
  // when do you win
  // if(es > width*1.5) {win = true;} 
  
  // if myMan is too small make 50
  if (es < 50) { es = 50; }
  
  // make myMan bigger or smaller when it hits other companies
  for (int i = companies.size()-1; i >= 0; i--) {
    Company c = (Company) companies.get(i);
    if (
      c.display().x > mouseX - ((c.cSize()/2)+(es/2)) && 
      c.display().x < mouseX + ((c.cSize()/2)+(es/2)) && 
      c.display().y > mouseY - ((c.cSize()/2)+(es/2)) && 
      c.display().y < mouseY + ((c.cSize()/2)+(es/2))
	  ) {

      // adjust size of myMan based on company size  
      if (es > c.cSize()) {
        es+=(c.cSize()/2);
      } 

      if (es < c.cSize()) {
        es-=(c.cSize()/8);
      }
	  
      companies.remove(i);
    }
  }
} 

void scene() {
  background(bgc);
}

// draw company circles
void populateCompany() {

  if (millis() > startTime + 1000) {
    startTime = millis(); // reset start time
    counter ++; // add to the counter
    int companyPick = int((random(mCaps.length))/2);
    companies.add(new Company(companyPick));
    once = 0;
  }

  // if (counter % 120 == 0 && once == 0) {
  //   mCaps = loadStrings("all.php");
  //   once = 1;
  // }
}

// show and remove company circles
void displayCompanies() {
  for (int i = companies.size()-1; i >= 0; i--) {
    Company c = (Company) companies.get(i);
    c.display();
  }

  for (int i = companies.size()-1; i >= 0; i--) {
    Company c = (Company) companies.get(i);
    if (c.display().y > height) {
      companies.remove(i);
    }
  }
}

// a class for creating each company circle
class Company {
  int sValue;
  int ex;
  int ey;
  int ewh;
  PVector[] pairs;

  Company(int sv) {
    sValue = sv;
    ex = int(random(width));
    pairs = new PVector[int(mCaps.length/2)];
  }

  // outputs a PVector with x = to market value and y = to compnay stock symbol
  PVector pairing(int n) {
    for (int i = 0; i < pairs.length; i++) {
      pairs[i] = new PVector(i*2, (i*2)+1);
    }

    PVector p = new PVector(pairs[n].x, pairs[n].y);

    if (p.y > width) { 
      p.y = width/1.5;
    }
    if (p.y < 2) { 
      p.y = 2;
    }
    float m = map(p.y, 2, width/2, 2, width/2);
    p.y = int(m);

    return p;
  }

  int mCap() {
    int x = mCaps[int(pairing(sValue).y)];
    return x;
  }

  String symbol() {
    String x = mCaps[int(pairing(sValue).x)];
    return x;
  }

  int cSize() {
    return ewh;
  }

  PVector display() {
    ewh = mCap();
    ey++;
    fill(250);
    noFill();
    ellipse(ex, ey, ewh, ewh);
    fill(225, 100, 100);
    int ts = int(ewh/5);
    if (ts < 10) {
      ts = 15;
    }
    textSize(ts);
    textAlign(CENTER);
    text(symbol(), ex, ey);
    PVector CompanyPosition = new PVector(ex, ey);
    return CompanyPosition;
  }
}

