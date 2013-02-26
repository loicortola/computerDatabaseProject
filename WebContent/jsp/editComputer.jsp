<h1>Edit computer</h1>



<form action="/computers/381" method="POST">

	<fieldset>
		<div class="clearfix ">
			<label for="name">Computer name</label>
			<div class="input">

				<input type="text" id="name" name="name" value="ACE"> <span
					class="help-inline">Required</span>
			</div>
		</div>
		<div class="clearfix ">
			<label for="introduced">Introduced date</label>
			<div class="input">

				<input type="text" id="introduced" name="introduced" value="">

				<span class="help-inline">Date (&#x27;yyyy-MM-dd&#x27;)</span>
			</div>
		</div>
		<div class="clearfix ">
			<label for="discontinued">Discontinued date</label>
			<div class="input">

				<input type="text" id="discontinued" name="discontinued" value="">

				<span class="help-inline">Date (&#x27;yyyy-MM-dd&#x27;)</span>
			</div>
		</div>
		<div class="clearfix ">
			<label for="company">Company</label>
			<div class="input">

				<select id="company" name="company">

					<option class="blank" value="">-- Choose a company --</option>


					<option value="22">Acorn computer</option>

					<option value="29">ACVS</option>

					<option value="14">Amiga Corporation</option>

					<option value="38">Amstrad</option>

					<option value="1">Apple Inc.</option>

					<option value="37">ASUS</option>

					<option value="20">Atari</option>

					<option value="35">BBN Technologies</option>

					<option value="15">Canon</option>

					<option value="6">Commodore International</option>

					<option value="31">Cray</option>

					<option value="10">Digital Equipment Corporation</option>

					<option value="33">E.S.R. Inc.</option>

					<option value="32">Evans &amp; Sutherland</option>

					<option value="27">Hewlett-Packard</option>

					<option value="41">HTC Corporation</option>

					<option value="13">IBM</option>

					<option value="9">IMS Associates, Inc.</option>

					<option value="36">Lenovo Group</option>

					<option value="11">Lincoln Laboratory</option>

					<option value="8">Micro Instrumentation and Telemetry
						Systems</option>

					<option value="12">Moore School of Electrical Engineering</option>

					<option value="7">MOS Technology</option>

					<option value="4">Netronics</option>

					<option value="19">NeXT</option>

					<option value="24">Nintendo</option>

					<option value="16">Nokia</option>

					<option value="34">OMRON</option>

					<option value="18">OQO</option>

					<option value="3">RCA</option>

					<option value="42">Research In Motion</option>

					<option value="43">Samsung Electronics</option>

					<option value="30">Sanyo</option>

					<option value="25">Sinclair Research Ltd</option>

					<option value="17">Sony</option>

					<option value="39">Sun Microsystems</option>

					<option value="5">Tandy Corporation</option>

					<option value="40">Texas Instruments</option>

					<option value="2">Thinking Machines</option>

					<option value="23">Timex Sinclair</option>

					<option value="26">Xerox</option>

					<option value="28">Zemmix</option>

				</select> <span class="help-inline"></span>
			</div>
		</div>
	</fieldset>

	<div class="actions">
		<input type="submit" value="Save this computer" class="btn primary">
		or <a href="/computers" class="btn">Cancel</a>
	</div>


</form>

<form action="/computers/381/delete" method="POST" class="topRight">

	<input type="submit" value="Delete this computer" class="btn danger">

</form>