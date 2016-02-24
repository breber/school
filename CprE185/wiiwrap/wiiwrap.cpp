// _______________________________________________________________________________
//
//	 - WiiYourself! - native C++ Wiimote library  v0.92b.
//	  (c) gl.tter 2007 - http://wiiyourself.gl.tter.org
//   - Wiiwrap wrapper tool for stdout reporting of wiimote data \
//    (c) Thomas Daniels 2007 - Iowa State Univertsity, Ames, IA
//	  see License.txt for conditions of use.  see History.txt for change log.
// _______________________________________________________________________________
//
//  wiiwrap.cpp  (tab = 4 spaces) based on demo.cpp
#include <stdio.h>
#include "wiiwrap.h"
#include "..\wiimote.h"
#include <mmsystem.h>	// for timeGetTime
int _oldtmain();

// ------------------------------------------------------------------------------------
//  simple callback example (we use polling for everything else):
// ------------------------------------------------------------------------------------

int outcount=0;
DWORD StartTime;
int header =0;
int cal = 0;

char * wiiheader(wiimote &w, char choice, char* outs, int size) {
        switch( choice) {

                case 'c':
                case 'C':
                        _snprintf               (outs,size, "%s", "Count");
                        break;
                case 't':
                case 'T':
                        _snprintf(outs,size, "%s", "time(ms)");
                        break;
                case 'a':
                case 'A':
                        _snprintf(outs,size, "a_x, a_y, a_z");
                        break;
                case 'r':
                case 'R':
                        _snprintf(outs,size, "ra_x, ra_y, ra_z");
                        break;

                case 'i':
                case 'I':
                        _snprintf(outs, size, "ir0_?, ir0_x, ir0_y, ir0_s, ir1_?, ir1_x, ir1_y, ir1_s");
                        break;
                case 'b':
                case 'B':
                        _snprintf(outs, size, "A, B, +, H, -, 1, 2, U, D, L, R");
                        break;
                default:
                        return NULL;

        }


//				ret= wiioutput(wiimote, toprint[i], buffer, 100);
char* wiioutput(wiimote &w, char choice, char* outs, int size) {
	switch( choice) {
		
		case 'c':
		case 'C':
			_snprintf		(outs,size, "%d", ++outcount);		
			break;
		case 't':
		case 'T':
			_snprintf(outs,size, "%d", timeGetTime()-StartTime);		
			break;
		case 'a':
		case 'A':
			_snprintf(outs,size, "%f, %f, %f", w.Acceleration.X, w.Acceleration.Y, w.Acceleration.Z);
			break;
		case 'r':
		case 'R':
			_snprintf(outs,size, "%d, %d, %d", w.Acceleration.RawX, w.Acceleration.RawY, w.Acceleration.RawZ);
			break;

		case 'i':
		case 'I':
			_snprintf(outs, size, "%d, %d, %d, %d, %d, %d, %d, %d", w.IR.Dot[0].bFound, w.IR.Dot[0].RawX, w.IR.Dot[0].RawY, w.IR.Dot[0].Size,
	w.IR.Dot[1].bFound, w.IR.Dot[1].RawX, w.IR.Dot[1].RawY, w.IR.Dot[1].Size);
			break;
		case 'b':
		case 'B':
			_snprintf(outs, size, "%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", w.Button.A(),
				w.Button.B(),				w.Button.Plus(),
				w.Button.Home(),
				w.Button.Minus(),
				w.Button.One(),
				w.Button.Two(),
				w.Button.Up(),
				w.Button.Down(),
				w.Button.Left(),
				w.Button.Right());
			break;
		default:
			return NULL;

	}
	
	return outs;
}




void on_state_change (wiimote &wiimote, state_change_flags changed)
	{
	// extension was just connected:
	if(changed & (NUNCHUK_CONNECTED|CLASSIC_CONNECTED))
		{
		Beep(1000, 200);
		// switch to a report mode that includes the extension data (we will
		//  loose the IR dot sizes)
		wiimote.SetReportType(wiimote::IN_BUTTONS_ACCEL_IR_EXT);
		}
	// extension was just disconnected:
	else if(changed & EXTENSION_DISCONNECTED)
		{
		Beep(200, 300);
		// use a non-extension report mode (this gives us back the IR dot sizes)
		wiimote.SetReportType(wiimote::IN_BUTTONS_ACCEL_IR);
		}
	}
// ------------------------------------------------------------------------------------

int ParseCommandLine(int argc, char*argv[], char *toprint) 
	{
	int i;
	int ret = 0;
	for (i=1; i<argc; i++) {
		if (argv[i][0] == '/' && (strlen(argv[i])==2) )
			{
				strcat(toprint, &(argv[i][1]));
				ret++;
			}
		else {fprintf(stderr, "Bad Argument to wiiwrap %s\n", argv[i]);}
		}
	return ret;
	}




int main(int argc, char *argv[]) { 
	// create a wiimote object
	wiimote wiimote;
	char toprint[100];  
	char buffer[101];
	toprint[0]=NULL;
	strcat(toprint, "cr");
	ParseCommandLine(argc, argv, toprint);

	// simple callback example (we use polling for almost everything here):
	//  notify us when something related to the extension changes
	wiimote.ChangedCallback		 = on_state_change;
	//  avoid unwanted callback overhead by requesting only extension-related data
	wiimote.CallbackTriggerFlags = EXTENSION_CHANGED;


	
	while(!wiimote.Connect(wiimote::FIRST_AVAILABLE)) {
			Sleep(1000);

	}
	StartTime=timeGetTime();	
		wiimote.SetLEDs(0x0f);
	printf("Connected\n");
		if(wiimote.bExtension)
		wiimote.SetReportType(wiimote::IN_BUTTONS_ACCEL_IR_EXT); // no IR dots
	else
		wiimote.SetReportType(wiimote::IN_BUTTONS_ACCEL_IR);	 //    IR dots
 

        // if needed print calibration info
        if (!(strpbrk(toprint, "Ll"))) {

                printf("Calibration:, X0, %d, Y0, %d, Z0, %d, XG, %d, YG, %d, ZG, %d", wiimote.CalibrationInfo.X0,
                        wiimote.CalibrationInfo.Y0, wiimote.CalibrationInfo.Z0,
                        wiimote.CalibrationInfo.XG, wiimote.CalibrationInfo.wiimote.YG, CalibrationInfo.ZG);

        }

 
	// if needed, print the column headings
	if (!(strpbrk(toprint, "hH")) {
		for (i=0; i < strlen(toprint);i++)
                {
                                ret= wiiheader(wiimote, toprint[i], buffer, 100);
                                if (ret) {
                                        if (i>0) printf(",");
                                        printf("%s", buffer);
                                        }

                }
		printf("/n");
	}
 	


	//Begin polling and printing state
	do	{

		// as we're polling, we must refresh the state for each pass:
		while(wiimote.RefreshState() == NO_CHANGE)
			Sleep(1); // // don't hog the CPU if nothing changed
		
		// Go through options, printing as requested.
		int i;
		char * ret;
		for (i=0; i < strlen(toprint);i++)
		{
				ret= wiioutput(wiimote, toprint[i], buffer, 100);
				if (ret) { 
					if (i>0) printf(",");
					printf("%s", buffer);
					}
				
		}
		printf("\n");


	}	while(!wiimote.Button.Home());


	// disconnect (auto-happens on wiimote destruction anyway, but let's play nice)
	wiimote.Disconnect();
	
	return 0;
//	return _oldtmain();
}

int _oldtmain ()
	{
	SetConsoleTitle(_T("- WiiYourself! - Demo: "));
	HANDLE console = GetStdHandle(STD_OUTPUT_HANDLE);

	// write the title
	BRIGHT_WHITE;
	_tprintf(_T("\n")); 
	_tprintf(_T("   -WiiYourself!- "));
	WHITE; _tprintf(		   _T("library Demo:   "));
	CYAN;  _tprintf(						   _T("| (c) "));
	BRIGHT_CYAN;  _tprintf(							 _T("gl"));
	BRIGHT_PURPLE;_tprintf(                            _T("."));
	BRIGHT_CYAN;  _tprintf(								_T("tter"));
	CYAN; _tprintf(										    _T(" 2007\n")
			 _T("                        v") WIIYOURSELF_VERSION_STR
										 _T("    |  http://wiiyourself.gl.tter.org\n"));
	CYAN;_tprintf(_T(" ______________________________________________________________________\n\n\n"));

	// let's load a couple of samples:
	wiimote_sample sine_sample, daisy_sample;

	// one .raw (just to demonstrate it)
	if(!wiimote::Load16BitMonoSampleRAW(_T("1kSine16 (3130).raw"),
										true, FREQ_3130HZ, sine_sample)) {
		_tprintf(_T("\r  ** can't find 'Sine16 (3130).raw' - (sample won't work!) **"));
		Beep(100, 1000);
		Sleep(3000);
		_tprintf(_T("\r") BLANK_LINE);
		}
	// and one (more convenient) .wav 
	if(!wiimote::Load16bitMonoSampleWAV(_T("Daisy16 (3130).wav"), daisy_sample)) {
		_tprintf(_T("\r  ** can't find 'Daisy16 (3130).wav' - (sample won't work!) **"));
		Beep(100, 1000);
		Sleep(3000);
		_tprintf(_T("\r") BLANK_LINE);
		}

	// create a wiimote object
	wiimote wiimote;
	
	// simple callback example (we use polling for almost everything here):
	//  notify us when something related to the extension changes
	wiimote.ChangedCallback		 = on_state_change;
	//  avoid unwanted callback overhead by requesting only extension-related data
	wiimote.CallbackTriggerFlags = EXTENSION_CHANGED;

reconnect:
	COORD pos = { 0, 6 };
	SetConsoleCursorPosition(console, pos);

	// try to connect the first available wiimote in the system
	//  (available means 'installed, and currently Bluetooth-connected'):
	WHITE; _tprintf(_T("  Looking for a Wiimote     "));
	   
	static const TCHAR* wait_str[] = { _T(".  "), _T(".. "), _T("...") };
	unsigned count = 0;
	while(!wiimote.Connect(wiimote::FIRST_AVAILABLE)) {
		_tprintf(_T("\b\b\b\b%s "), wait_str[count%3]);
		count++;
		Beep(500, 30); Sleep(1000);
		}

	// connected - light all LEDs
	wiimote.SetLEDs(0x0f);
	BRIGHT_CYAN; _tprintf(_T("\b\b\b\b... connected!"));
	Beep(1000, 300); Sleep(2000);

	// ask the wiimote to report everything (using the 'non-continous updates'
	//  default mode - updates will be frequent anyway due to the acceleration/IR
	//  values changing):

	// *note*: the report mode that includes the extension data unfortunately
	//			only reports the 'BASIC' IR info (ie. no dot sizes) - so let's choose
	//          the best mode based on the extension status (we also toggle modes
	//			as needed in the callback above):
	if(wiimote.bExtension)
		wiimote.SetReportType(wiimote::IN_BUTTONS_ACCEL_IR_EXT); // no IR dots
	else
		wiimote.SetReportType(wiimote::IN_BUTTONS_ACCEL_IR);	 //    IR dots

	// print the button event instructions:
	BRIGHT_WHITE;
	_tprintf(_T("\r  -- TRY: B = rumble, A = square, 1 = sine, 2 = daisy, Home = Exit --\n"));

	// display the wiimote output (until 'Home' is pressed):
	DWORD	 last_rumble_time = timeGetTime(); // for rumble text animation
	bool	 rumble_text	  = true;
	DWORD    last_led_time    = timeGetTime(); // for led         animation
	unsigned lit_led          = 0;	
	
	do	{
		COORD pos = { 0, 8 };
		SetConsoleCursorPosition(console, pos);

		// as we're polling, we must refresh the state for each pass:
		while(wiimote.RefreshState() == NO_CHANGE)
			Sleep(1); // // don't hog the CPU if nothing changed

		// did we loose the connection?
		if(wiimote.ConnectionLost())
			{
			BRIGHT_RED; _tprintf(
				_T("   *** connection lost! ***                                          \n")
				BLANK_LINE BLANK_LINE BLANK_LINE BLANK_LINE BLANK_LINE BLANK_LINE
				BLANK_LINE BLANK_LINE BLANK_LINE BLANK_LINE BLANK_LINE BLANK_LINE
				BLANK_LINE BLANK_LINE BLANK_LINE);
			Beep(100, 1000);
			Sleep(2000);
			COORD pos = { 0, 6 };
			SetConsoleCursorPosition(console, pos);
			_tprintf(BLANK_LINE BLANK_LINE BLANK_LINE);
			goto reconnect;
			}

		// rumble if 'B' (trigger) is pressed
		wiimote.SetRumble(wiimote.Button.B());

		// actions for buttons just pressed/released:
		static bool last_A = false, last_One = false, last_Two = false;

		#define ON_PRESS_RELEASE(button, pressed_action, released_action)	\
			{ bool pressed = wiimote.Button.##button();						\
			  if(pressed)													\
			 	{				   /* just pressed? */						\
				if(!last_##button) pressed_action;							\
				}															\
			else if(last_##button) /* just released */						\
				released_action;											\
			/* remember the current button state for next time */			\
			last_##button = pressed; }
			
		//  play audio whilst certain buttons are held
		ON_PRESS_RELEASE(  A, wiimote.PlaySquareWave(FREQ_3130HZ, 0x20), 
							  wiimote.EnableSpeaker(false));
		ON_PRESS_RELEASE(One, wiimote.PlaySample	(sine_sample),
							  wiimote.EnableSpeaker(false));
		ON_PRESS_RELEASE(Two, wiimote.PlaySample	(daisy_sample),
							  wiimote.EnableSpeaker(false));

		// Battery:
		CYAN; _tprintf(_T("  Battery: "));
		// (the green/yellow/red colour ranges are rough guesses - my wiimote
		//  with rechargeable battery pack fails at around 15%)
		if	   (wiimote.BatteryPercent >= 30) BRIGHT_GREEN;
		else if(wiimote.BatteryPercent >= 20) BRIGHT_YELLOW; else BRIGHT_RED;
		_tprintf(_T("% 3u%%   "), wiimote.BatteryPercent);

		DWORD current_time = timeGetTime();

		// LEDs:
		//  animate them every second
		if((current_time - last_led_time) >= 1000) {
			wiimote.SetLEDs(1<<lit_led);
			lit_led		  = (++lit_led) % 4;
			last_led_time = timeGetTime();
			}

		CYAN; _tprintf(_T("LEDs: ")); WHITE; _tprintf(_T("["));
		for(unsigned led=0; led<4; led++)
			{
			if(wiimote.LED.Lit(led)) {
				BRIGHT_CYAN; _tprintf(_T("*"));
				}
			else{
				WHITE      ; _tprintf(_T("-"));//_T("%c"), '0'+led);
				}
			}

		// Rumble
		WHITE; _tprintf(_T("]    "));
		if(wiimote.bRumble) {
			BRIGHT_WHITE; _tprintf(rumble_text? _T(" RUMBLE") : _T("RUMBLE "));
			// animate the text
			if((current_time - last_rumble_time) >= 110) {
				rumble_text		 = !rumble_text;
				last_rumble_time = current_time;
				}
			}
		else
			_tprintf(_T("       "));

		// Output method:
	    CYAN; _tprintf( _T("    using %s\n"), (wiimote.IsUsingHIDwrites()?
											   _T("HID writes") : _T("WriteFile()")));
		
		// Buttons:
		CYAN; _tprintf(_T("\n  Buttons: ")); WHITE; _tprintf(_T("["));
		for(unsigned bit=0; bit<16; bit++)
			{
			WORD mask = 1 << bit;
			// skip unused bits
			if((wiimote_state::buttons::ALL & mask) == 0)
				continue;

			const TCHAR* button_name = wiimote::ButtonNameFromBit[bit];
			bool		 pressed	 = ((wiimote.Button.Bits & mask) != 0);
			if(bit > 0) {
				CYAN; _tprintf(_T("|")); // seperator
				}
			if(pressed) {
				BRIGHT_WHITE; _tprintf(_T("%s")  , button_name);
				}
			else{
				WHITE       ; _tprintf(_T("% *s"), _tcslen(button_name), _T(""));
				}
			}
		WHITE; _tprintf(_T("]\n"));

		// Acceleration:
		CYAN ; _tprintf(_T("    Accel:"));
		WHITE; _tprintf(_T("  X %+2.3f  Y %+2.3f  Z %+2.3f  \n"),
						wiimote.Acceleration.X,
						wiimote.Acceleration.Y,
						wiimote.Acceleration.Z);
	
		// Orientation estimate (shown red if last valid update is aging):
		CYAN ; _tprintf(_T("   Orient:"));
		WHITE; _tprintf(_T("  UpdateAge % 3u  "),
						wiimote.Acceleration.Orientation.UpdateAge);
		
		//  show if the last orientation update is considered out-of-date
		//   (using an arbitrary threshold)
		if(wiimote.Acceleration.Orientation.UpdateAge > 10)
			RED;
			
		_tprintf(_T("Pitch % 4ddeg  Roll % 4ddeg  \n")
			     _T("                           (X %+.3f  Y %+.3f  Z %+.3f)      \n"),
				(int)wiimote.Acceleration.Orientation.Pitch,
				(int)wiimote.Acceleration.Orientation.Roll ,
				wiimote.Acceleration.Orientation.X,
				wiimote.Acceleration.Orientation.Y,
  				wiimote.Acceleration.Orientation.Z);
				
		// IR:
		CYAN ; _tprintf(_T("       IR:"));
		WHITE; _tprintf(_T("  Mode %s  "),
			((wiimote.IR.Mode == wiimote_state::ir::OFF     )? _T("OFF  ") :
			 (wiimote.IR.Mode == wiimote_state::ir::BASIC   )? _T("BASIC") :
			 (wiimote.IR.Mode == wiimote_state::ir::EXTENDED)? _T("EXT. ") :
															   _T("FULL ")));
		// IR dot sizes are only reported in EXTENDED IR mode (FULL isn't supported yet)
		bool dot_sizes = (wiimote.IR.Mode == wiimote_state::ir::EXTENDED);

		//  dot 1:
		_tprintf(_T("1: "));
		if(wiimote.IR.Dot[0].bFound) WHITE; else RED;
		_tprintf(_T("%s  "), (wiimote.IR.Dot[0].bFound? _T("Found    ") :
														_T("Not found")));

		_tprintf(_T("Size"));
		 if(dot_sizes)
			 _tprintf(_T("% 3d "), wiimote.IR.Dot[0].Size);
		else{
			RED; _tprintf(_T(" n/a"));
			if(wiimote.IR.Dot[0].bFound) WHITE;
			}

		_tprintf(_T("  X %.3f  Y %.3f\n"), wiimote.IR.Dot[0].X,
										   wiimote.IR.Dot[0].Y);
		//  dot 2:
		WHITE; _tprintf(_T("                        2: "));
		if(wiimote.IR.Dot[1].bFound) WHITE; else RED;
		_tprintf(_T("%s  "), (wiimote.IR.Dot[1].bFound? _T("Found    ") :
														_T("Not found")));
		
		_tprintf(_T("Size"));
		 if(dot_sizes)
			 _tprintf(_T("% 3d "), wiimote.IR.Dot[1].Size);
		else{
			RED; _tprintf(_T(" n/a"));
			if(wiimote.IR.Dot[1].bFound) WHITE;
			}

		_tprintf(_T("  X %.3f  Y %.3f\n"), wiimote.IR.Dot[1].X,
										   wiimote.IR.Dot[1].Y);
		// Speaker:
		CYAN ; _tprintf(_T("  Speaker:"));
		WHITE; _tprintf(_T("  %s | %s    "),
									(wiimote.Speaker.bEnabled? _T("On ") :
															   _T("Off")),
									(wiimote.Speaker.bMuted  ? _T("Muted") :
															   _T("     ")));
		if(!wiimote.Speaker.bEnabled || wiimote.Speaker.bMuted)
			RED;
		else//if(wiimote.IsPlayingAudio()) BRIGHT_WHITE; else WHITE;
			WHITE;
		_tprintf(_T("Frequency % 4u Hz   Volume 0x%02x\n"),
				 wiimote::FreqLookup[wiimote.Speaker.Freq],
				 wiimote.Speaker.Volume);
		
		// -- Extension --
		CYAN ; _tprintf(_T("__________\n\n  Extnsn.:  "));
		switch(wiimote.ExtensionType)
			{
			case wiimote_state::NONE:
			case wiimote_state::PARTIALLY_INSERTED:
				{
				if(wiimote.ExtensionType == wiimote_state::PARTIALLY_INSERTED) {
					BRIGHT_RED;
					_tprintf(_T("Partially Inserted                                               \n"));
					}
				else{
					RED;
					_tprintf(_T("None                                                             \n"));
					}
				_tprintf(BLANK_LINE BLANK_LINE BLANK_LINE);
				}
				break;
			
			// -- Nunchuk --
			case wiimote_state::NUNCHUK:
				{
				BRIGHT_WHITE; _tprintf(_T("Nunchuk   "));

				// Buttons:
				CYAN		; _tprintf(_T("Buttons: ")); WHITE; _tprintf(_T("["));
				BRIGHT_WHITE; _tprintf(wiimote.Nunchuk.C? _T("C") : _T(" "));
				CYAN		; _tprintf(_T("|"));
				BRIGHT_WHITE; _tprintf(wiimote.Nunchuk.Z? _T("Z") : _T(" "));
				WHITE		; _tprintf(_T("]   "));
				// Joystick:
				CYAN		; _tprintf(_T("Joystick:  "));
				WHITE; _tprintf(_T("X %+2.3f  Y %+2.3f\n"),
										wiimote.Nunchuk.Joystick.X,
										wiimote.Nunchuk.Joystick.Y);
				// Acceleration:
				CYAN ; _tprintf(_T("    Accel:"));
				WHITE; _tprintf(_T("  X %+2.3f  Y %+2.3f  Z %+2.3f  \n"),
						wiimote.Nunchuk.Acceleration.X,
						wiimote.Nunchuk.Acceleration.Y,
						wiimote.Nunchuk.Acceleration.Z);
				
				// Orientation estimate (shown red if last valid update is aging):
				CYAN ; _tprintf(_T("   Orient:"));
				WHITE; _tprintf(_T("  UpdateAge % 3u  "),
								wiimote.Nunchuk.Acceleration.Orientation.UpdateAge);
				//  show if the last orientation update is aging
				if(wiimote.Nunchuk.Acceleration.Orientation.UpdateAge > 10)
					RED;
				_tprintf(_T("Pitch % 4ddeg  Roll % 4ddeg  \n")
					     _T("                           (X %+.2f  Y %+.2f  Z %+.2f)      \n"),
						 (int)wiimote.Nunchuk.Acceleration.Orientation.Pitch,
						 (int)wiimote.Nunchuk.Acceleration.Orientation.Roll ,
						 wiimote.Nunchuk.Acceleration.Orientation.X,
						 wiimote.Nunchuk.Acceleration.Orientation.Y,
  						 wiimote.Nunchuk.Acceleration.Orientation.Z);
				}
				break;

			// -- Classic Controller --
			case wiimote_state::CLASSIC:
				{
				BRIGHT_WHITE; _tprintf(_T("Classic Controller   "));
				
				// L: Joystick/Trigger
				WHITE; _tprintf(_T("L:  "));
				CYAN ; _tprintf(_T("Joy "));
				WHITE; _tprintf(_T("X %+2.3f  Y %+2.3f  "),
								wiimote.ClassicController.JoystickL.X,
								wiimote.ClassicController.JoystickL.Y);
				CYAN ; _tprintf(_T("Trig "));
				WHITE; _tprintf(_T("%+2.3f\n"),
								wiimote.ClassicController.TriggerL);
				// R: Joystick/Trigger
				WHITE; _tprintf(_T("                                 R:  "));
				CYAN ; _tprintf(_T("Joy "));
				WHITE; _tprintf(_T("X %+2.3f  Y %+2.3f  "),
								wiimote.ClassicController.JoystickR.X,
								wiimote.ClassicController.JoystickR.Y);
				CYAN ; _tprintf(_T("Trig "));
				WHITE; _tprintf(_T("%+2.3f\n"),
								wiimote.ClassicController.TriggerR);

				// Buttons:
				CYAN; _tprintf(_T("  Buttons: ")); WHITE; _tprintf(_T("["));
				for(unsigned bit=0; bit<16; bit++)
					{
					WORD mask = 1 << bit;
					// skip unused bits
					if((wiimote_state::classic_controller::buttons::ALL & mask) == 0)
						continue;

					const TCHAR* button_name = wiimote::ClassicButtonNameFromBit[bit];
					bool		 pressed	 = ((wiimote.ClassicController.Button.Bits & mask) != 0);
					const TCHAR* seperator	 = (bit==0)? _T("") : _T("|");
					CYAN; _tprintf(seperator);
					if(pressed) {
						BRIGHT_WHITE; _tprintf(_T("%s")  , button_name);
						}
					else{
						WHITE		; _tprintf(_T("% *s"), _tcslen(button_name), _T(""));
						}
					}
				WHITE; _tprintf(_T("]"));
				}
				break;
			}
  		}
		while(!wiimote.Button.Home());


	// disconnect (auto-happens on wiimote destruction anyway, but let's play nice)
	wiimote.Disconnect();
	Beep(1000, 200);

	BRIGHT_WHITE; // for automatic 'press any key to continue' msg
	CloseHandle(console);

	return 0;
	}
// ------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------