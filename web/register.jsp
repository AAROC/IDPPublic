<% pageContext.setAttribute("title", "Register"); %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="WEB-INF/jspf/header.jspf" %>
                    <h2>Register</h2>

<script type="text/javascript">
(function( $ ){
	$.fn.captcha = function(options){
	var defaults = {  
	   borderColor: "",  
	   captchaDir: "captcha",   
	   formId: "myForm",  
	   text: "Verify that you are a human,<br />drag <span>scissors</span> into the circle."
	  };	
	
	var options = $.extend(defaults, options); 

		
	$(this).html("<b class='ajax-fc-rtop'><b class='ajax-fc-r1'></b> <b class='ajax-fc-r2'></b> <b class='ajax-fc-r3'></b> <b class='ajax-fc-r4'></b></b><img class='ajax-fc-border' id='ajax-fc-left' src='" + options.captchaDir + "/imgs/border-left.png' /><img class='ajax-fc-border' id='ajax-fc-right' src='" + options.captchaDir + "/imgs/border-right.png' /><div id='ajax-fc-content'><div id='ajax-fc-left'><p id='ajax-fc-task'>" + options.text + "</p><ul id='ajax-fc-task'><li class='ajax-fc-0'><img src='" + options.captchaDir + "/imgs/item-none.png' alt='' /></li><li class='ajax-fc-1'><img src='" + options.captchaDir + "/imgs/item-none.png' alt='' /></li><li class='ajax-fc-2'><img src='" + options.captchaDir + "/imgs/item-none.png' alt='' /></li><li class='ajax-fc-3'><img src='" + options.captchaDir + "/imgs/item-none.png' alt='' /></li><li class='ajax-fc-4'><img src='" + options.captchaDir + "/imgs/item-none.png' alt='' /></li></ul></div><div id='ajax-fc-right'><a target='_blank' href='http://www.webdesignbeach.com'><img id='ajax-fc-backlink' src='" + options.captchaDir + "/imgs/wdb.png' alt='Web Design Beach' /></a><p id='ajax-fc-circle'></p></div></div><div id='ajax-fc-corner-spacer'></div><b class='ajax-fc-rbottom'><b class='ajax-fc-r4'></b> <b class='ajax-fc-r3'></b> <b class='ajax-fc-r2'></b> <b class='ajax-fc-r1'></b></b>");
	 $("p#ajax-fc-task span").html("<s:bean name="it.infn.ct.security.actions.CaptchaAction"><s:property value="%{res}" />");
    
        <s:property value="%{imgs}" escape=""/></s:bean>;
	$(".ajax-fc-container, .ajax-fc-rtop *, .ajax-fc-rbottom *").css("background-color", options.borderColor);
	$("#ajax-fc-circle").droppable({
		drop: function(event, ui) {
			//ui.draggable("disable");
			$("#addUser_captcha").attr('value', ui.draggable.find('img').attr('id'));
		},
		tolerance: 'touch'
	});
        };
 })( jQuery );
 
 function randomNumber() {
	var chars = "01234";
	chars += ".";
	var size = 1;
	var i = 1;
	var ret = "";
		while ( i <= size ) {
			$max = chars.length-1;
			$num = Math.floor(Math.random()*$max);
			$temp = chars.substr($num, 1);
			ret += $temp;
			i++;
		}
	return ret;
}

function other(){
    if($("#organization").val() == "Other:")
        document.getElementById("desc").style.display="table-row";
    else 
        document.getElementById("desc").style.display="none";
}

function fillOrgs(){
    var select=document.getElementById("organization");
    var c=$("#country").val();
    select.options.length=1;
    for(i in orgs){
        if(orgs[i].countryCode == c){
            var opt=document.createElement("option");
            opt.text=orgs[i].key+" ("+orgs[i].description+")";
            opt.value=orgs[i].key;
            opt.title=orgs[i].description;
            try {
                select.add(opt, null); // standards compliant; doesn't work in IE
            }
            catch(ex) {
                select.add(opt); // IE only
            }
        }
    }
    try {
        select.add(new Option("Other:","Other:"), null); // standards compliant; doesn't work in IE
    }
    catch(ex) {
        select.add(new Option("Other:","Other:")); // IE only
    }
}

function tmpOrg(key, countryCode, description){
    this.key=key;
    this.countryCode=countryCode;
    this.description=description;
    
}

$(function() {
        $(".ajax-fc-container").captcha({
                borderColor: "silver",
                text: "Verify that you are a human,<br />drag <span>scissors</span> into the circle."
        });
});

var orgs=new Array();
<s:action name="OrgAction" executeResult="true" />

</script>
<br />
            <s:form action="addUser.action" method="POST">
                <tr>
                    <td>
                        <p>
                            <h2>Personal information</h2>
                        </p>
                </td></tr>
                <s:textfield label="Given Name" name="givenname" maxLength="50" required="true" />
                <s:textfield label="Family Name" name="surname" maxLength="50" required="true" />
                <s:textfield label="Title" name="title" maxLength="50" />
                <s:textfield label="Address" name="address" maxLength="255" />
                <s:textfield label="Phone" name="phone" maxLength="50" />
                <s:textfield label="Desired username" name="username" maxLength="50" required="true" />
                <s:password label="Password" name="password" maxLength="50" required="true" />
                <s:password label="Confirm password" name="password2" maxLength="50" required="true" />
                <tr>
                    <td class="tdLabel">
                        <label for="country" class="label">Country<span class="required">*</span>:</label>
                    </td> 
                    <td>
                        <select onchange="fillOrgs()" name="country" id="country">
                            <option value="-1" selected="selected">Select Country</option> 
                            <option value="AX">Åland Islands</option>
                            <option value="AF">Afghanistan</option>
                            <option value="AL">Albania</option>
                            <option value="DZ">Algeria</option>
                            <option value="AS">American Samoa</option>
                            <option value="AD">Andorra</option>
                            <option value="AO">Angola</option>
                            <option value="AI">Anguilla</option>
                            <option value="AQ">Antarctica</option>
                            <option value="AG">Antigua and Barbuda</option>
                            <option value="AR">Argentina</option>
                            <option value="AM">Armenia</option>
                            <option value="AW">Aruba</option>
                            <option value="AU">Australia</option>
                            <option value="AT">Austria</option>
                            <option value="AZ">Azerbaijan</option>
                            <option value="BS">Bahamas</option>
                            <option value="BH">Bahrain</option>
                            <option value="BD">Bangladesh</option>
                            <option value="BB">Barbados</option>
                            <option value="BY">Belarus</option>
                            <option value="BE">Belgium</option>
                            <option value="BZ">Belize</option>
                            <option value="BJ">Benin</option>
                            <option value="BM">Bermuda</option>
                            <option value="BT">Bhutan</option>
                            <option value="BO">Bolivia, Plurinational State Of</option>
                            <option value="BQ">Bonaire, Sint Eustatius and Saba</option>
                            <option value="BA">Bosnia and Herzegovina</option>
                            <option value="BW">Botswana</option>
                            <option value="BV">Bouvet Island</option>
                            <option value="BR">Brazil</option>
                            <option value="IO">British Indian Ocean Territory</option>
                            <option value="BN">Brunei Darussalam</option>
                            <option value="BG">Bulgaria</option>
                            <option value="BF">Burkina Faso</option>
                            <option value="BI">Burundi</option>
                            <option value="KH">Cambodia</option>
                            <option value="CM">Cameroon</option>
                            <option value="CA">Canada</option>
                            <option value="CV">Cape Verde</option>
                            <option value="KY">Cayman Islands</option>
                            <option value="CF">Central African Republic</option>
                            <option value="TD">Chad</option>
                            <option value="CL">Chile</option>
                            <option value="CN">China</option>
                            <option value="CX">Christmas Island</option>
                            <option value="CC">Cocos (Keeling) Islands</option>
                            <option value="CO">Colombia</option>
                            <option value="KM">Comoros</option>
                            <option value="CG">Congo</option>
                            <option value="CD">Congo, The Democratic Republic Of The</option>
                            <option value="CK">Cook Islands</option>
                            <option value="CR">Costa Rica</option>
                            <option value="HR">Croatia</option>
                            <option value="CU">Cuba</option>
                            <option value="CW">Curaçao</option>
                            <option value="CY">Cyprus</option>
                            <option value="CZ">Czech Republic</option>
                            <option value="CI">Côte D'Ivoire</option>
                            <option value="DK">Denmark</option>
                            <option value="DJ">Djibouti</option>
                            <option value="DM">Dominica</option>
                            <option value="DO">Dominican Republic</option>
                            <option value="EC">Ecuador</option>
                            <option value="EG">Egypt</option>
                            <option value="SV">El Salvador</option>
                            <option value="GQ">Equatorial Guinea</option>
                            <option value="ER">Eritrea</option>
                            <option value="EE">Estonia</option>
                            <option value="ET">Ethiopia</option>
                            <option value="EU">Europe</option>
                            <option value="FK">Falkland Islands (Malvinas)</option>
                            <option value="FO">Faroe Islands</option>
                            <option value="FJ">Fiji</option>
                            <option value="FI">Finland</option>
                            <option value="FR">France</option>
                            <option value="GF">French Guiana</option>
                            <option value="PF">French Polynesia</option>
                            <option value="TF">French Southern Territories</option>
                            <option value="GA">Gabon</option>
                            <option value="GM">Gambia</option>
                            <option value="GE">Georgia</option>
                            <option value="DE">Germany</option>
                            <option value="GH">Ghana</option>
                            <option value="GI">Gibraltar</option>
                            <option value="GR">Greece</option>
                            <option value="GL">Greenland</option>
                            <option value="GD">Grenada</option>
                            <option value="GP">Guadeloupe</option>
                            <option value="GU">Guam</option>
                            <option value="GT">Guatemala</option>
                            <option value="GG">Guernsey</option>
                            <option value="GN">Guinea</option>
                            <option value="GW">Guinea-Bissau</option>
                            <option value="GY">Guyana</option>
                            <option value="HT">Haiti</option>
                            <option value="HM">Heard Island and McDonald Islands</option>
                            <option value="VA">Holy See (Vatican City State)</option>
                            <option value="HN">Honduras</option>
                            <option value="HK">Hong Kong</option>
                            <option value="HU">Hungary</option>
                            <option value="IS">Iceland</option>
                            <option value="IN">India</option>
                            <option value="ID">Indonesia</option>
                            <option value="IR">Iran, Islamic Republic Of</option>
                            <option value="IQ">Iraq</option>
                            <option value="IE">Ireland</option>
                            <option value="IM">Isle of Man</option>
                            <option value="IL">Israel</option>
                            <option value="IT">Italy</option>
                            <option value="JM">Jamaica</option>
                            <option value="JP">Japan</option>
                            <option value="JE">Jersey</option>
                            <option value="JO">Jordan</option>
                            <option value="KZ">Kazakhstan</option>
                            <option value="KE">Kenya</option>
                            <option value="KI">Kiribati</option>
                            <option value="KP">Korea, Democratic People's Republic Of</option>
                            <option value="KR">Korea, Republic of</option>
                            <option value="KW">Kuwait</option>
                            <option value="KG">Kyrgyzstan</option>
                            <option value="LA">Lao People's Democratic Republic</option>
                            <option value="LV">Latvia</option>
                            <option value="LB">Lebanon</option>
                            <option value="LS">Lesotho</option>
                            <option value="LR">Liberia</option>
                            <option value="LY">Libya</option>
                            <option value="LI">Liechtenstein</option>
                            <option value="LT">Lithuania</option>
                            <option value="LU">Luxembourg</option>
                            <option value="MO">Macao</option>
                            <option value="MK">Macedonia, the Former Yugoslav Republic Of</option>
                            <option value="MG">Madagascar</option>
                            <option value="MW">Malawi</option>
                            <option value="MY">Malaysia</option>
                            <option value="MV">Maldives</option>
                            <option value="ML">Mali</option>
                            <option value="MT">Malta</option>
                            <option value="MH">Marshall Islands</option>
                            <option value="MQ">Martinique</option>
                            <option value="MR">Mauritania</option>
                            <option value="MU">Mauritius</option>
                            <option value="YT">Mayotte</option>
                            <option value="MX">Mexico</option>
                            <option value="FM">Micronesia, Federated States Of</option>
                            <option value="MD">Moldova, Republic of</option>
                            <option value="MC">Monaco</option>
                            <option value="MN">Mongolia</option>
                            <option value="ME">Montenegro</option>
                            <option value="MS">Montserrat</option>
                            <option value="MA">Morocco</option>
                            <option value="MZ">Mozambique</option>
                            <option value="MM">Myanmar</option>
                            <option value="NA">Namibia</option>
                            <option value="NR">Nauru</option>
                            <option value="NP">Nepal</option>
                            <option value="NL">Netherlands</option>
                            <option value="NC">New Caledonia</option>
                            <option value="NZ">New Zealand</option>
                            <option value="NI">Nicaragua</option>
                            <option value="NE">Niger</option>
                            <option value="NG">Nigeria</option>
                            <option value="NU">Niue</option>
                            <option value="NF">Norfolk Island</option>
                            <option value="MP">Northern Mariana Islands</option>
                            <option value="NO">Norway</option>
                            <option value="OM">Oman</option>
                            <option value="PK">Pakistan</option>
                            <option value="PW">Palau</option>
                            <option value="PS">Palestinian Territory, Occupied</option>
                            <option value="PA">Panama</option>
                            <option value="PG">Papua New Guinea</option>
                            <option value="PY">Paraguay</option>
                            <option value="PE">Peru</option>
                            <option value="PH">Philippines</option>
                            <option value="PN">Pitcairn</option>
                            <option value="PL">Poland</option>
                            <option value="PT">Portugal</option>
                            <option value="PR">Puerto Rico</option>
                            <option value="QA">Qatar</option>
                            <option value="RO">Romania</option>
                            <option value="RU">Russian Federation</option>
                            <option value="RW">Rwanda</option>
                            <option value="RE">Réunion</option>
                            <option value="BL">Saint Barthélemy</option>
                            <option value="SH">Saint Helena, Ascension and Tristan Da Cunha</option>
                            <option value="KN">Saint Kitts And Nevis</option>
                            <option value="LC">Saint Lucia</option>
                            <option value="MF">Saint Martin (French Part)</option>
                            <option value="PM">Saint Pierre And Miquelon</option>
                            <option value="VC">Saint Vincent And The Grenadines</option>
                            <option value="WS">Samoa</option>
                            <option value="SM">San Marino</option>
                            <option value="ST">Sao Tome and Principe</option>
                            <option value="SA">Saudi Arabia</option>
                            <option value="SN">Senegal</option>
                            <option value="RS">Serbia</option>
                            <option value="SC">Seychelles</option>
                            <option value="SL">Sierra Leone</option>
                            <option value="SG">Singapore</option>
                            <option value="SX">Sint Maarten (Dutch part)</option>
                            <option value="SK">Slovakia</option>
                            <option value="SI">Slovenia</option>
                            <option value="SB">Solomon Islands</option>
                            <option value="SO">Somalia</option>
                            <option value="ZA">South Africa</option>
                            <option value="GS">South Georgia and the South Sandwich Islands</option>
                            <option value="SS">South Sudan</option>
                            <option value="ES">Spain</option>
                            <option value="LK">Sri Lanka</option>
                            <option value="SD">Sudan</option>
                            <option value="SR">Suriname</option>
                            <option value="SJ">Svalbard And Jan Mayen</option>
                            <option value="SZ">Swaziland</option>
                            <option value="SE">Sweden</option>
                            <option value="CH">Switzerland</option>
                            <option value="SY">Syrian Arab Republic</option>
                            <option value="TW">Taiwan, Province Of China</option>
                            <option value="TJ">Tajikistan</option>
                            <option value="TZ">Tanzania, United Republic of</option>
                            <option value="TH">Thailand</option>
                            <option value="TL">Timor-Leste</option>
                            <option value="TG">Togo</option>
                            <option value="TK">Tokelau</option>
                            <option value="TO">Tonga</option>
                            <option value="TT">Trinidad and Tobago</option>
                            <option value="TN">Tunisia</option>
                            <option value="TR">Turkey</option>
                            <option value="TM">Turkmenistan</option>
                            <option value="TC">Turks and Caicos Islands</option>
                            <option value="TV">Tuvalu</option>
                            <option value="UG">Uganda</option>
                            <option value="UA">Ukraine</option>
                            <option value="AE">United Arab Emirates</option>
                            <option value="GB">United Kingdom</option>
                            <option value="US">United States</option>
                            <option value="UM">United States Minor Outlying Islands</option>
                            <option value="UY">Uruguay</option>
                            <option value="UZ">Uzbekistan</option>
                            <option value="VU">Vanuatu</option>
                            <option value="VE">Venezuela, Bolivarian Republic of</option>
                            <option value="VN">Viet Nam</option>
                            <option value="VG">Virgin Islands, British</option>
                            <option value="VI">Virgin Islands, U.S.</option>
                            <option value="WF">Wallis and Futuna</option>
                            <option value="EH">Western Sahara</option>
                            <option value="YE">Yemen</option>
                            <option value="ZM">Zambia</option>
                            <option value="ZW">Zimbabwe</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="tdLabel">
                        <label for="organization" class="label">Organisation<span class="required">*</span>:</label>
                    </td> 
                    <td>
                        <select onchange="other()" name="organization" id="organization">
                            <option value="-1">Select Organisation</option>
                        </select>
                    </td>
                </tr>
                
                <tr id="desc" style="display:none;" >
                    <td>
                    </td>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    <label for="orgname" class="label">Name:</label>
                                </td>
                                <td>
                                    <input type="text" name="orgname" id="orgname" />
                                </td>
                                <td>
                                    <label for="description" class="label">Description:</label>
                                </td>
                                <td>
                                    <input type="text" name="description" id="description" />
                                </td>
                            </tr>
                        </table>
                        </div>
                    </td>
                </tr>
                <s:textfield label="Email Address" name="registeredMail" maxLength="128" required="true" />
                <s:textfield label="Additional Email Addresses" tooltip="Separate the addresses with a , or ;" name="otherMails" maxLength="512" />
                <tr><td><br /></td></tr>
                <tr><td><p><h2>Validation</h2></p></td></tr>
                <tr><td><br /></td></tr>
                <tr errorFor="captcha"><td><span class="errorMessage"><s:if test="fieldErrors.get('captcha').size() > 0">
                <s:property value="fieldErrors.get('captcha').get(0)" /></s:if></span></td></tr>
                <tr><td colspan="2"><div class="ajax-fc-container">You must enable javascript to see captcha here!</div> </td> </tr>
                <s:hidden name="captcha" />
                <tr><td><br /></td></tr>
                <s:submit key="register" align="center" />
            </s:form>

             <p><h2>Note</h2><p>
             <p><i>If you would change your password or other information please log-in to the IdP service following the <b>Account</b> link on the top right corner </i></p>
<%@include file="WEB-INF/jspf/footer.jspf" %>