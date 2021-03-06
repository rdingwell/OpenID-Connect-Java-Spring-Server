/*******************************************************************************
 * Copyright 2012 The MITRE Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mitre.openid.connect.view;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.openid.connect.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.view.AbstractView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Component("jsonUserInfoView")
public class JSONUserInfoView extends AbstractView {
	
	private static Logger logger = LoggerFactory.getLogger(JSONUserInfoView.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		
		UserInfo userInfo = (UserInfo) model.get("userInfo");

		Set<String> scope = (Set<String>) model.get("scope");
		
		Gson gson = new GsonBuilder()
			.setExclusionStrategies(new ExclusionStrategy() {
				
				public boolean shouldSkipField(FieldAttributes f) {
					
					return false;
				}
				
				public boolean shouldSkipClass(Class<?> clazz) {
					// skip the JPA binding wrapper
					if (clazz.equals(BeanPropertyBindingResult.class)) {
						return true;
					}
					return false;
				}
								
			}).create();

		response.setContentType("application/json");
		
		Writer out;
		
		try {
			
			out = response.getWriter();
			gson.toJson(toJson(userInfo, scope), out);
			
		} catch (IOException e) {
			
			logger.error("IOException in JSONUserInfoView.java: " + e.getStackTrace());
			
		}

	}
	
	private JsonObject toJson(UserInfo ui, Set<String> scope) {
		
		JsonObject obj = new JsonObject();
		
		if (scope.contains("openid")) {
			obj.addProperty("user_id", ui.getUserId());
		}
		
		if (scope.contains("profile")) {
			obj.addProperty("name", ui.getName());
			obj.addProperty("preferred_username", ui.getPreferredUsername());
			obj.addProperty("given_name", ui.getGivenName());
			obj.addProperty("family_name", ui.getFamilyName());
			obj.addProperty("middle_name", ui.getMiddleName());
			obj.addProperty("nickname", ui.getNickname());
			obj.addProperty("profile", ui.getProfile());
			obj.addProperty("picture", ui.getPicture());
			obj.addProperty("website", ui.getWebsite());
			obj.addProperty("gender", ui.getGender());
			obj.addProperty("zone_info", ui.getZoneinfo());
			obj.addProperty("locale", ui.getLocale());
			obj.addProperty("updated_time", ui.getUpdatedTime());
		}
		
		if (scope.contains("email")) {
			obj.addProperty("email", ui.getEmail());
			obj.addProperty("email_verified", ui.getEmailVerified());
		}
		
		if (scope.contains("phone")) {
			obj.addProperty("phone_number", ui.getPhoneNumber());
		}
		
		if (scope.contains("address") && ui.getAddress() != null) {

			JsonObject addr = new JsonObject();
			addr.addProperty("formatted", ui.getAddress().getFormatted());
			addr.addProperty("street_address", ui.getAddress().getStreetAddress());
			addr.addProperty("locality", ui.getAddress().getLocality());
			addr.addProperty("region", ui.getAddress().getRegion());
			addr.addProperty("postal_code", ui.getAddress().getPostalCode());
			addr.addProperty("country", ui.getAddress().getCountry());
			
			obj.add("address", addr);
		}

		
		return obj;
	}
}
