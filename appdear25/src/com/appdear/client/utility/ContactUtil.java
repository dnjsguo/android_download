package com.appdear.client.utility;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.Log;

import com.appdear.client.R;

public class ContactUtil {
	private static ContactUtil conutil = new ContactUtil();

	private ContactUtil() {

	}

	public static ContactUtil getInstance() {
		if (conutil != null) {
			return conutil;
		} else {
			return new ContactUtil();
		}
	}

	/*
	 * 判断字符串是否为空
	 */
	protected static boolean isStr(String str) {
		if (str != null && !str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	protected static Object getJsonObj(JSONObject json, String key) {
		if (json.has(key)) {
			try {
				if (json.get(key) == null) {
					return null;
				} else {
					return json.get(key);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}

	protected static Object getJsonObjForArray(JSONObject json, String key) {
		if (json.has(key)) {
			try {
				if (json.get(key) == null) {
					return null;
				} else {
					JSONArray jsons = null;
					if (json.get(key) instanceof JSONArray) {
						jsons = json.getJSONArray(key);
						if (jsons != null && jsons.length() >= 0) {
							return ((JSONObject) jsons.get(0)).get("value");
						}
					}
					return null;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}

	/**
	 * 获得通讯录信息
	 * 
	 * @param context
	 * @param page
	 *            当前页
	 * @param count
	 *            无用
	 * @param pagecount
	 *            每页显示记录数
	 * @return
	 */
	public static String handlerContact(Context context, int flag) {
		JSONObject json = new JSONObject();
		Cursor cur = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				new String[] { ContactsContract.Contacts._ID },
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
		if (flag == -1) {
			int length = (cur == null) ? 0 : cur.getCount();
			if (cur != null)
				cur.close();
			return String.valueOf(length);
		}
		// 循环遍历
		Cursor birthdays = null;
		try {
			json.put("protocoltype", "contacts");
			JSONArray contacts = new JSONArray();
			JSONObject contact = null;
			if (cur != null && cur.getCount() > 0) {
				int length = cur.getCount();
				int postion = 0;
				int end = 0;
				// int pages = (cur.getCount() % pagecount == 0 ? cur.getCount()
				// / pagecount : cur.getCount() / pagecount + 1);
				// if (page > pages)
				// return json.toString();
				// if (page <= 0)
				// page = 1;
				// postion = (page - 1) * pagecount;
				//
				// end = page * pagecount > cur.getCount() ? cur.getCount() :
				// page
				// * pagecount;
				if (cur.moveToPosition(postion)) {
					int idColumn = cur
							.getColumnIndex(ContactsContract.RawContacts._ID);
					int count1 = 0;
					do {
						JSONArray tel_home = null, mobile_home = null, tel_work = null, mobile_work = null, pager = null, tel_car = null, tel_data = null, tel_callback = null, tel_company_main = null, fax_home = null, fax_work = null, tel_pref = null, tel_mms = null, telephone = null, fax = null, tel_radio = null, tel_telex = null, tel_tty_tdd = null, tel_isdn = null, tel_workpage = null, tel_custom = null, tel_web = null;
						// if (postion >= end)
						if (postion >= cur.getCount())
							break;
						contact = new JSONObject();
						String contactId = cur.getString(idColumn);
						contact.put("id", new JSONArray().put(new JSONObject()
								.put("value", contactId)));
						// 生日信息
						birthdays = context.getContentResolver().query(
								Data.CONTENT_URI,
								new String[] { Data._ID, Data.MIMETYPE,
										Data.DATA1, Data.DATA2, Data.DATA3,
										Data.DATA4, Data.DATA5, Data.DATA6,
										Data.DATA7, Data.DATA8, Data.DATA9 },
								Data.CONTACT_ID + "=?",
								new String[] { contactId }, null);
						if (birthdays != null && birthdays.getCount() > 0) {
							if (birthdays.moveToFirst()) {
								JSONArray birthday = null, event_other = null, event_cuntom = null, event_anniversary = null, im_home = null, im_work = null, im = null, im_cuntom = null, organizations_work = null, organizations_other = null, organizations_custom = null, website_work = null, website_home = null, website_other = null, website_blog = null, website_ftp = null, website_homepage = null, website_profile = null, website_custom = null, compony = null, grouparray = null, email_home = null, email_work = null, email = null, email_mobile = null, email_custom = null, postal_private = null, postal_business = null, postal_other = null, postal_custom = null;
								do {
									String id = birthdays.getString(birthdays
											.getColumnIndex(Data._ID));
									String type = birthdays.getString(birthdays
											.getColumnIndex(Data.MIMETYPE));
									String minitype = birthdays
											.getString(birthdays
													.getColumnIndex(Data.DATA2));
									String date = birthdays.getString(birthdays
											.getColumnIndex(Data.DATA1));

									if (type != null && !type.equals("")) {
										if (type.equals(Email.CONTENT_ITEM_TYPE)) {
											if (minitype != null
													&& !minitype.equals("")) {
												switch (Integer
														.parseInt(minitype)) {
												case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
													if (email_home == null) {
														email_home = new JSONArray();
													}
													email_home
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));
													// contact.put("email_home",emailValue);
													break;
												case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
													if (email_work == null) {
														email_work = new JSONArray();
													}
													email_work
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));
													// contact.put("email_work",emailValue);
													break;
												case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
													if (email == null) {
														email = new JSONArray();
													}
													email.put(new JSONObject()
															.put("value", date)
															.put("id", id));
													break;
												case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
													if (email_mobile == null) {
														email_mobile = new JSONArray();
													}
													email_mobile
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));
													break;

												case ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM:
													if (email_custom == null) {
														email_custom = new JSONArray();
													}
													String custom = birthdays
															.getString(birthdays
																	.getColumnIndex(Data.DATA3));
													email_custom
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id)
																	.put("name",
																			custom));
													break;
												default:
													break;
												}
											}
										} else if (type
												.equals(Phone.CONTENT_ITEM_TYPE)) {
											if (minitype != null
													&& !minitype.equals("")) {
												switch (Integer
														.parseInt(minitype)) {
												case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
													if (tel_home == null) {
														tel_home = new JSONArray();
													}
													tel_home.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("tel_home",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
													if (mobile_home == null) {
														mobile_home = new JSONArray();
													}
													mobile_home
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));

													// contact.put("mobile_home",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
													if (tel_work == null) {
														tel_work = new JSONArray();
													}
													tel_work.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("tel_work",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
													if (mobile_work == null) {
														mobile_work = new JSONArray();
													}
													mobile_work
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));

													// contact.put("mobile_work",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
													if (pager == null) {
														pager = new JSONArray();
													}
													pager.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("pager",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
													if (tel_car == null) {
														tel_car = new JSONArray();
													}
													tel_car.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("tel_car",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
													if (tel_data == null) {
														tel_data = new JSONArray();
													}
													tel_data.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("tel_data",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
													if (tel_callback == null) {
														tel_callback = new JSONArray();
													}
													tel_callback
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));

													// contact.put("tel_callback",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
													if (tel_company_main == null) {
														tel_company_main = new JSONArray();
													}
													tel_company_main
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));

													// contact.put("tel_company_main",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
													if (fax_home == null) {
														fax_home = new JSONArray();
													}
													fax_home.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("fax_home",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
													if (fax_work == null) {
														fax_work = new JSONArray();
													}
													fax_work.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("fax_work",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
													if (tel_pref == null) {
														tel_pref = new JSONArray();
													}
													tel_pref.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("tel_pref",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:

													if (tel_mms == null) {
														tel_mms = new JSONArray();
													}
													tel_mms.put(new JSONObject()
															.put("value", date)
															.put("id", id));

													// contact.put("tel_mms",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:

													if (tel_workpage == null) {
														tel_workpage = new JSONArray();
													}
													tel_workpage
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));

													// contact.put("tel_mms",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:

													if (tel_custom == null) {
														tel_custom = new JSONArray();
													}
													String custom = birthdays
															.getString(birthdays
																	.getColumnIndex(Data.DATA3));
													tel_custom
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id)
																	.put("name",
																			custom));
													// contact.put("tel_mms",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
													if (telephone == null) {
														telephone = new JSONArray();
													}
													// contact.put("telephone",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
													if (fax == null) {
														fax = new JSONArray();
													}
													fax.put(new JSONObject()
															.put("value", date)
															.put("id", id));
													// contact.put("fax",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
													if (tel_radio == null) {
														tel_radio = new JSONArray();
													}
													tel_radio
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));
													// contact.put("tel_radio",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
													if (tel_telex == null) {
														tel_telex = new JSONArray();
													}
													tel_telex
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));

													// contact.put("tel_telex",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
													if (tel_tty_tdd == null) {
														tel_tty_tdd = new JSONArray();
													}
													tel_tty_tdd
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));

													// contact.put("tel_tty_tdd",phoneNumber);
													break;
												case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
													if (tel_isdn == null) {
														tel_isdn = new JSONArray();
													}
													tel_isdn.put(new JSONObject()
															.put("value", date)
															.put("id", id));
													break;
												default:
													break;
												}
											} else {
												if (date != null
														&& !date.equals("")) {
													if (tel_web == null) {
														tel_web = new JSONArray();
													}
													tel_web.put(new JSONObject()
															.put("value", date)
															.put("id", id));
												}
											}
										} else if (type
												.equals(Event.CONTENT_ITEM_TYPE)) {
											if (minitype != null
													&& !minitype.equals("")) {
												switch (Integer
														.parseInt(minitype)) {
												case Event.TYPE_ANNIVERSARY:
													if (event_anniversary == null) {
														event_anniversary = new JSONArray();
													}
													event_anniversary
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));
													// contact.put("email_home",emailValue);
													break;
												case Event.TYPE_BIRTHDAY:
													if (birthday == null) {
														birthday = new JSONArray();
													}
													birthday.put(new JSONObject()
															.put("value", date)
															.put("id", id));
													// contact.put("email_work",emailValue);
													break;
												case Event.TYPE_OTHER:
													if (event_other == null) {
														event_other = new JSONArray();
													}
													event_other
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id));
													// contact.put("email_work",emailValue);
													break;
												case Event.TYPE_CUSTOM:
													if (event_cuntom == null) {
														event_cuntom = new JSONArray();
													}
													String custom = birthdays
															.getString(birthdays
																	.getColumnIndex(Data.DATA3));
													event_cuntom
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("id",
																			id)
																	.put("name",
																			custom));

													// contact.put("email_work",emailValue);
													break;
												default:
													break;
												}
											}
										} else if (type
												.equals(Im.CONTENT_ITEM_TYPE)) {
											String protocol = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA5));
											if (minitype != null
													&& !minitype.equals("")) {
												switch (Integer
														.parseInt(minitype)) {
												case Im.TYPE_HOME:
													if (im_home == null) {
														im_home = new JSONArray();
													}
													im_home.put(new JSONObject()
															.put("value", date)
															.put("protocol",
																	protocol)
															.put("id", id));
													// contact.put("email_home",emailValue);
													break;
												case Im.TYPE_WORK:
													if (im_work == null) {
														im_work = new JSONArray();
													}
													im_work.put(new JSONObject()
															.put("value", date)
															.put("protocol",
																	protocol)
															.put("id", id));
													// contact.put("email_work",emailValue);
													break;
												case Im.TYPE_OTHER:
													if (im == null) {
														im = new JSONArray();
													}
													im.put(new JSONObject()
															.put("value", date)
															.put("protocol",
																	protocol)
															.put("id", id));
													break;

												case Im.TYPE_CUSTOM:
													if (im_cuntom == null) {
														im_cuntom = new JSONArray();
													}
													String custom = birthdays
															.getString(birthdays
																	.getColumnIndex(Data.DATA3));
													im_cuntom
															.put(new JSONObject()
																	.put("value",
																			date)
																	.put("protocol",
																			protocol)
																	.put("id",
																			id)
																	.put("name",
																			custom));
													break;
												default:
													if (im == null) {
														im = new JSONArray();
													}
													im.put(new JSONObject()
															.put("value", date)
															.put("protocol",
																	protocol)
															.put("id", id));
													break;
												}
											}
										} else if (type
												.equals(Organization.CONTENT_ITEM_TYPE)) {
											String title = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA4));
											String department = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA5));
											String dscribtion = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA6));
											if (minitype != null
													&& !minitype.equals("")) {
												switch (Integer
														.parseInt(minitype)) {
												case ContactsContract.CommonDataKinds.Organization.TYPE_WORK:
													if (organizations_work == null) {
														organizations_work = new JSONArray();
													}
													// 2011-09-25 jd 添加公司信息
													if (compony == null) {
														compony = new JSONArray();
													}
													compony.put(new JSONObject()
															.put("value",
																	(date == null ? ""
																			: date)));
													organizations_work
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			(date == null ? ""
																					: date))
																	.put("dec",
																			(dscribtion == null ? ""
																					: dscribtion))
																	.put("department",
																			(department == null ? ""
																					: department))
																	.put("title",
																			(title == null ? ""
																					: title
																							+ " ")));
													// contact.put("postal_private",formatAddress);
													break;
												case ContactsContract.CommonDataKinds.Organization.TYPE_OTHER:
													if (organizations_other == null) {
														organizations_other = new JSONArray();
													}
													organizations_other
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			(date == null ? ""
																					: date))
																	.put("dec",
																			(dscribtion == null ? ""
																					: dscribtion))
																	.put("department",
																			(department == null ? ""
																					: department))
																	.put("title",
																			(title == null ? ""
																					: title
																							+ " ")));
													// contact.put("postal_business",formatAddress);
													break;
												case ContactsContract.CommonDataKinds.Organization.TYPE_CUSTOM:
													if (organizations_custom == null) {
														organizations_custom = new JSONArray();
													}
													organizations_custom
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			(date == null ? ""
																					: date))
																	.put("dec",
																			(dscribtion == null ? ""
																					: dscribtion))
																	.put("department",
																			(department == null ? ""
																					: department))
																	.put("title",
																			(title == null ? ""
																					: title
																							+ " ")));
													// contact.put("postal_other",formatAddress);
													break;
												default:
													break;
												}
											}

										} else if (type
												.equals(Website.CONTENT_ITEM_TYPE)) {
											if (minitype != null
													&& !minitype.equals("")) {
												if (minitype
														.equals(Website.TYPE_HOME)) {
													if (website_home == null) {
														website_home = new JSONArray();
													}
													website_home
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date));

												} else if (minitype
														.equals(Website.TYPE_WORK)) {
													if (website_work == null) {
														website_work = new JSONArray();
													}
													website_work
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date));
												} else if (minitype
														.equals(Website.TYPE_BLOG)) {
													if (website_blog == null) {
														website_blog = new JSONArray();
													}
													website_blog
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date));
												} else if (minitype
														.equals(Website.TYPE_FTP)) {
													if (website_ftp == null) {
														website_ftp = new JSONArray();
													}
													website_ftp
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date));
												} else if (minitype
														.equals(Website.TYPE_HOMEPAGE)) {
													if (website_homepage == null) {
														website_homepage = new JSONArray();
													}
													website_homepage
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date));
												} else if (minitype
														.equals(Website.TYPE_PROFILE)) {
													if (website_profile == null) {
														website_profile = new JSONArray();
													}
													website_profile
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date));
												} else if (minitype
														.equals(Website.TYPE_CUSTOM)) {
													if (website_custom == null) {
														website_custom = new JSONArray();

													}
													String custom = birthdays
															.getString(birthdays
																	.getColumnIndex(Data.DATA3));
													website_custom
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date)
																	.put("name",
																			custom));
												} else if (minitype
														.equals(Website.TYPE_OTHER)) {
													if (website_other == null) {
														website_other = new JSONArray();
													}
													website_other
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date));
												} else {
													if (date != null
															&& !date.equals("")) {
														if (website_other == null) {
															website_other = new JSONArray();
														}
														website_other
																.put(new JSONObject()
																		.put("id",
																				id)
																		.put("value",
																				date));
													}
												}

											} else {
												if (date != null
														&& !date.equals("")) {
													if (website_other == null) {
														website_other = new JSONArray();
													}
													website_other
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date));
												}
											}
										} else if (type
												.equals(GroupMembership.CONTENT_ITEM_TYPE)) {
											if (grouparray == null) {
												grouparray = new JSONArray();
											}
											grouparray.put(new JSONObject()
													.put("id", id).put("value",
															date));
										} else if (type
												.equals(Note.CONTENT_ITEM_TYPE)) {
											contact.put(
													"title",
													new JSONArray()
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date)));
										} else if (type
												.equals(Nickname.CONTENT_ITEM_TYPE)) {
											contact.put(
													"nickname",
													new JSONArray()
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("value",
																			date)));
										} else if (type
												.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
											// contact.put("firstname",new
											// JSONObject().put("id",id).put("value",date));

											String data3 = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA3));

											String data5 = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA5));
											if (isStr(date)) {
												contact.put(
														"name",
														new JSONArray()
																.put(new JSONObject()
																		.put("value",
																				date)));
												contact.put(
														"firstname",
														new JSONArray()
																.put(new JSONObject()
																		.put("value",
																				date)));
											} else {
												StringBuffer sb = new StringBuffer(
														"");
												if (isStr(minitype)) {
													sb.append(minitype).append(
															" ");
												}
												if (isStr(data5)) {
													sb.append(data5)
															.append(" ");
													contact.put(
															"middlename",
															new JSONArray()
																	.put(new JSONObject()
																			.put("value",
																					data5)));
												}
												if (isStr(data3)) {
													sb.append(data3)
															.append(" ");
													contact.put(
															"lastname",
															new JSONArray()
																	.put(new JSONObject()
																			.put("value",
																					data3)));
												}
												if (sb.length() > 0) {
													sb.deleteCharAt(sb.length() - 1);
													contact.put(
															"firstname",
															new JSONArray()
																	.put(new JSONObject()
																			.put("value",
																					sb.toString())));
												}
											}

											contact.put(
													"nameid",
													new JSONArray()
															.put(new JSONObject()
																	.put("value",
																			id)));
										} else if (type
												.equals(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)) {
											String street = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA4));
											String country = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA6));
											String city = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA7));
											String region = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA8));
											String postcode = birthdays
													.getString(birthdays
															.getColumnIndex(Data.DATA9));
											if (minitype != null
													&& !minitype.equals("")) {

												switch (Integer
														.parseInt(minitype)) {
												case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME:
													if (postal_private == null) {
														postal_private = new JSONArray();
													}
													postal_private
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("city",
																			city)
																	.put("country",
																			country)
																	.put("street",
																			street)
																	.put("postcode",
																			postcode)
																	.put("region",
																			region));
													// contact.put("postal_private",formatAddress);
													break;
												case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK:
													if (postal_business == null) {
														postal_business = new JSONArray();
													}
													postal_business
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("city",
																			city)
																	.put("country",
																			country)
																	.put("street",
																			street)
																	.put("postcode",
																			postcode)
																	.put("region",
																			region));
													// contact.put("postal_business",formatAddress);
													break;
												case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER:
													if (postal_other == null) {
														postal_other = new JSONArray();
													}
													postal_other
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("city",
																			city)
																	.put("country",
																			country)
																	.put("street",
																			street)
																	.put("postcode",
																			postcode)
																	.put("region",
																			region));
													// contact.put("postal_other",formatAddress);
													break;
												case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM:
													if (postal_custom == null) {
														postal_custom = new JSONArray();
													}
													String custom = birthdays
															.getString(birthdays
																	.getColumnIndex(Data.DATA3));
													postal_custom
															.put(new JSONObject()
																	.put("id",
																			id)
																	.put("city",
																			city)
																	.put("country",
																			country)
																	.put("street",
																			street)
																	.put("postcode",
																			postcode)
																	.put("region",
																			region)
																	.put("name",
																			custom));
													// contact.put("postal_custom",formatAddress);
													break;
												default:
													break;
												}
											}
										}
									}
								} while (birthdays.moveToNext());

								// if(contact==null){
								//
								// length--;
								// // Log.i("in", length+"");
								// continue;
								// }
								if (event_anniversary != null) {
									contact.put("event_anniversary",
											event_anniversary);
								}
								if (birthday != null) {
									contact.put("birthday", birthday);
								}
								if (event_other != null) {
									contact.put("event_other", event_other);
								}
								if (event_cuntom != null) {
									contact.put("event_cuntom", event_cuntom);
								}

								/*
								 * 如果 im 为空 将 im_home || im_work || im_cuntom
								 * 赋值给它
								 */
								if (im != null) {
									contact.put("im", im);
								}
								if (im_home != null) {
									if (im == null) {
										contact.put("im", im_home);
									}
									contact.put("im_home", im_home);
								}
								if (im_work != null) {
									if (im == null && im_home == null) {
										contact.put("im", im_work);
									}
									contact.put("im_work", im_work);
								}
								if (im_cuntom != null) {
									if (im == null && im_home == null
											&& im_work == null) {
										contact.put("im", im_cuntom);
									}
									contact.put("im_cuntom", im_cuntom);
								}
								/*----------------------------------------*/

								if (organizations_work != null) {
									contact.put("organizations_work",
											organizations_work);
								}
								if (organizations_other != null) {
									contact.put("organizations",
											organizations_other);
								}
								if (organizations_custom != null) {
									contact.put("organizations_custom",
											organizations_custom);
								}

								/*
								 * 如果 web 为空 将 website_home || website_work ||
								 * website_blog || website_ftp ||
								 * website_homepage 赋值给它
								 */
								if (website_other != null) {
									contact.put("web", website_other);
								}
								if (website_home != null) {
									if (website_other == null) {
										contact.put("web", website_home);
									}
									contact.put("web_home", website_home);
								}
								if (website_work != null) {
									if (website_other == null
											&& website_home == null) {
										contact.put("web", website_work);
									}
									contact.put("web_work", website_work);
								}
								if (website_blog != null) {
									if (website_other == null
											&& website_home == null
											&& website_work == null) {
										contact.put("web", website_blog);
									}
									contact.put("web_blog", website_blog);
								}
								if (website_ftp != null) {
									if (website_other == null
											&& website_home == null
											&& website_work == null
											&& website_blog == null) {
										contact.put("web", website_ftp);
									}
									contact.put("web_ftp", website_ftp);
								}
								if (website_homepage != null) {
									if (website_other == null
											&& website_home == null
											&& website_work == null
											&& website_blog == null
											&& website_ftp == null) {
										contact.put("web", website_homepage);
									}
									contact.put("web_homepage",
											website_homepage);
								}
								if (website_profile != null) {
									if (website_other == null
											&& website_home == null
											&& website_work == null
											&& website_blog == null
											&& website_ftp == null
											&& website_homepage == null) {
										contact.put("web", website_profile);
									}
									contact.put("web_profile", website_profile);
								}
								if (website_custom != null) {
									if (website_other == null
											&& website_home == null
											&& website_work == null
											&& website_blog == null
											&& website_ftp == null
											&& website_homepage == null
											&& website_profile == null) {
										contact.put("web", website_custom);
									}
									contact.put("web_custom", website_custom);
								}
								/*----------------------------------------*/

								/*
								 * 如果 telephone 为空 将 tel_home || tel_work ||
								 * tel_pref || tel_car || tel_data 赋值给它
								 */
								if (tel_home != null) {
									contact.put("telephone", tel_home);
								}
								if (tel_work != null) {
									if (tel_home == null) {
										contact.put("telephone", tel_work);
									}
									contact.put("tel_work", tel_work);
								}
								if (tel_pref != null) {
									if (tel_home == null && tel_work == null) {
										contact.put("telephone", tel_pref);
									}
									contact.put("tel_pref", tel_pref);
								}
								if (tel_car != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null) {
										contact.put("telephone", tel_car);
									}
									contact.put("tel_car", tel_car);
								}
								if (tel_data != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null) {
										contact.put("telephone", tel_data);
									}
									contact.put("tel_data", tel_data);
								}
								if (tel_mms != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null
											&& tel_data == null) {
										contact.put("telephone", tel_mms);
									}
									contact.put("tel_mms", tel_mms);
								}
								if (tel_radio != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null
											&& tel_data == null
											&& tel_mms == null) {
										contact.put("telephone", tel_radio);
									}
									contact.put("tel_radio", tel_radio);
								}
								if (tel_telex != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null
											&& tel_data == null
											&& tel_mms == null
											&& tel_radio == null) {
										contact.put("telephone", tel_telex);
									}
									contact.put("tel_telex", tel_telex);
								}
								if (tel_tty_tdd != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null
											&& tel_data == null
											&& tel_mms == null
											&& tel_radio == null
											&& tel_telex == null) {
										contact.put("telephone", tel_tty_tdd);
									}
									contact.put("tel_tty_tdd", tel_tty_tdd);
								}
								if (tel_isdn != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null
											&& tel_data == null
											&& tel_mms == null
											&& tel_radio == null
											&& tel_telex == null
											&& tel_tty_tdd == null) {
										contact.put("telephone", tel_isdn);
									}
									contact.put("tel_isdn", tel_isdn);
								}
								if (telephone != null) {
									if (tel_home == null) {
										contact.put("tel_home", telephone);
									}
									// contact.put("tel_home", telephone);
								}
								if (tel_workpage != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null
											&& tel_data == null
											&& tel_mms == null
											&& tel_radio == null
											&& tel_telex == null
											&& tel_tty_tdd == null
											&& tel_isdn == null) {
										contact.put("telephone", tel_workpage);
									}
									contact.put("tel_workpage", tel_workpage);
								}
								if (tel_custom != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null
											&& tel_data == null
											&& tel_mms == null
											&& tel_radio == null
											&& tel_telex == null
											&& tel_tty_tdd == null
											&& tel_isdn == null
											&& tel_workpage == null) {
										contact.put("telephone", tel_custom);
									}
									contact.put("tel_custom", tel_custom);
								}
								if (tel_web != null) {
									if (tel_home == null && tel_work == null
											&& tel_pref == null
											&& tel_car == null
											&& tel_data == null
											&& tel_mms == null
											&& tel_radio == null
											&& tel_telex == null
											&& tel_tty_tdd == null
											&& tel_isdn == null
											&& tel_workpage == null
											&& tel_custom == null) {
										contact.put("telephone", tel_web);
									}
									contact.put("tel_web", tel_web);
								}
								/*
								 * ----------------------------------------------
								 */

								if (pager != null) {
									contact.put("pager", pager);
								}

								if (tel_callback != null) {
									contact.put("tel_callback", tel_callback);
								}
								if (tel_company_main != null) {
									contact.put("tel_company_main",
											tel_company_main);
								}

								/*
								 * 如果 mobile 为空 将 mobile_home || mobile_work
								 * 赋值给它
								 */
								if (mobile_home != null) {
									contact.put("mobile", mobile_home);
								}
								if (mobile_work != null) {
									if (mobile_home == null) {
										contact.put("mobile", mobile_work);
									}
									contact.put("mobile_work", mobile_work);
								}
								/*
								 * ----------------------------------------------
								 */

								/*
								 * 如果 fax 为空 将 fax_home || fax_work 赋值给它
								 */
								if (fax != null) {
									contact.put("fax", fax);
								}
								if (fax_home != null) {
									if (fax == null) {
										contact.put("fax", fax_home);
									}
									contact.put("fax_home", fax_home);
								}
								if (fax_work != null && fax_home == null) {
									if (fax == null && fax_home == null) {
										contact.put("fax", fax_work);
									}
									contact.put("fax_work", fax_work);
								}
								/*
								 * ----------------------------------------------
								 */

								/*
								 * 如果 email 为空 将 email_home || email_work ||
								 * email_mobile || email_custom 赋值给它
								 */
								if (email != null) {
									contact.put("email_home", email);
								}
								if (email_home != null) {
									if (email == null) {
										contact.put("email", email_home);
									}
									// contact.put("email", email_home);
								}
								if (email_work != null) {
									if (email == null && email_home == null) {
										contact.put("email", email_work);
									}
									contact.put("email_work", email_work);
								}
								if (email_mobile != null) {
									if (email == null && email_home == null
											&& email_work == null) {
										contact.put("email", email_mobile);
									}
									contact.put("email_mobile", email_mobile);
								}
								if (email_custom != null) {
									if (email == null && email_home == null
											&& email_work == null
											&& email_mobile == null) {
										contact.put("email", email_custom);
									}
									contact.put("email_custom", email_custom);
								}
								/*
								 * ----------------------------------------------
								 */

								if (postal_private != null) {
									contact.put("postal_private",
											postal_private);
								}
								if (postal_business != null) {
									contact.put("postal_business",
											postal_business);
								}
								if (postal_other != null) {
									contact.put("postal_other", postal_other);
								}
								if (postal_custom != null) {
									contact.put("postal_custom", postal_custom);
								}
								if (grouparray != null) {
									contact.put("group", grouparray);
								}
								if (compony != null) {
									contact.put("company", compony);
								}

							}
						}
						if (birthdays != null)
							birthdays.close();
						contacts.put(contact);
						postion++;
						// count++;
						count1++;
						// Log.i("json",(System.currentTimeMillis()-time)+"="+count1);
					} while (cur.moveToNext());
					// Log.i("json","total="+(System.currentTimeMillis()-t));
					// }
					// Log.i("in",contacts+"");
				}
				json.put("count", length + "");
				json.put("contacts", contacts);
			}else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (cur != null)
				cur.close();
			if (birthdays != null)
				birthdays.close();
		}
		return json.toString();

	}

	public static int handlerContactAdd(Context context, JSONObject json,
			int type) {
		int count = 0;
		ArrayList<ContentProviderOperation> list = new ArrayList<ContentProviderOperation>();
		ContentValues values = new ContentValues();
		// 向联系人执行一个空值插入，目的是获取系统返回的联系人id
		try {
			JSONArray jsonarray = json.getJSONArray("contacts");
			if (jsonarray != null && jsonarray.length() >= 1) {
				for (int i = 0; i < jsonarray.length(); i++) {
					long rawContactId = 0;
					json = jsonarray.getJSONObject(i);
					values.clear();

					// 解析json中联系ID
					rawContactId = (getJsonObjForArray(json, "id") != null) ? Long
							.parseLong(getJsonObjForArray(json, "id")
									.toString()) : 0;
					if (rawContactId == 0)
						continue;
					else {
						// 判断 接口返回后解析出来的rawContactId 是否在数据库中存在
						if (!ContactUtil.getPhoneContactsById(context,
								rawContactId)) { // 如果不存在执行插入
							type = 0;
							// 0添加联系人
							if (type == 0) {
								Uri rawContactUri = null;
								rawContactUri = context
										.getContentResolver()
										.insert(ContactsContract.RawContacts.CONTENT_URI,
												values);
								rawContactId = ContentUris
										.parseId(rawContactUri);
							} else {
								// 修改联系人
								rawContactId = (getJsonObjForArray(json, "id") != null) ? Long
										.parseLong(getJsonObjForArray(json,
												"id").toString()) : 0;
								if (rawContactId == 0)
									continue;
							}
							addTelephone(list, context,
									getJsonObj(json, "mobile"), values,
									rawContactId, Phone.TYPE_MOBILE, type);
							addTelephone(list, context,
									getJsonObj(json, "telephone"), values,
									rawContactId, Phone.TYPE_HOME, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_home"), values,
									rawContactId, Phone.TYPE_OTHER, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_work"), values,
									rawContactId, Phone.TYPE_WORK, type);
							addTelephone(list, context,
									getJsonObj(json, "mobile_work"), values,
									rawContactId, Phone.TYPE_WORK_MOBILE, type);
							addTelephone(list, context,
									getJsonObj(json, "pager"), values,
									rawContactId, Phone.TYPE_PAGER, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_car"), values,
									rawContactId, Phone.TYPE_CAR, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_pref"), values,
									rawContactId, Phone.TYPE_MAIN, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_data"), values,
									rawContactId, Phone.TYPE_ASSISTANT, type);
							addTelephone(list, context,
									getJsonObj(json, "fax"), values,
									rawContactId, Phone.TYPE_OTHER_FAX, type);
							addTelephone(list, context,
									getJsonObj(json, "fax_home"), values,
									rawContactId, Phone.TYPE_FAX_HOME, type);
							addTelephone(list, context,
									getJsonObj(json, "fax_work"), values,
									rawContactId, Phone.TYPE_FAX_WORK, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_mms"), values,
									rawContactId, Phone.TYPE_MMS, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_radio"), values,
									rawContactId, Phone.TYPE_RADIO, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_telex"), values,
									rawContactId, Phone.TYPE_TELEX, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_tty_tdd"), values,
									rawContactId, Phone.TYPE_TTY_TDD, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_isdn"), values,
									rawContactId, Phone.TYPE_ISDN, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_callback"), values,
									rawContactId, Phone.TYPE_CALLBACK, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_company_main"),
									values, rawContactId,
									Phone.TYPE_COMPANY_MAIN, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_workpage"), values,
									rawContactId, Phone.TYPE_WORK_PAGER, type);
							addTelephone(list, context,
									getJsonObj(json, "tel_custom"), values,
									rawContactId, Phone.TYPE_CUSTOM, type);
							addEmail(list, context,
									getJsonObj(json, "email_home"), values,
									rawContactId, Email.TYPE_OTHER, type);
							addEmail(list, context, getJsonObj(json, "email"),
									values, rawContactId, Email.TYPE_HOME, type);
							addEmail(list, context,
									getJsonObj(json, "email_mobile"), values,
									rawContactId, Email.TYPE_MOBILE, type);
							addEmail(list, context,
									getJsonObj(json, "email_custom"), values,
									rawContactId, Email.TYPE_CUSTOM, type);
							addEmail(list, context,
									getJsonObj(json, "email_work"), values,
									rawContactId, Email.TYPE_WORK, type);
							addAddress(list, context,
									getJsonObj(json, "postal_business"),
									values, rawContactId,
									StructuredPostal.TYPE_WORK, type);
							addAddress(list, context,
									getJsonObj(json, "postal_custom"), values,
									rawContactId, StructuredPostal.TYPE_CUSTOM,
									type);
							addAddress(list, context,
									getJsonObj(json, "postal"), values,
									rawContactId, StructuredPostal.TYPE_OTHER,
									type);
							addAddress(list, context,
									getJsonObj(json, "postal_private"), values,
									rawContactId, StructuredPostal.TYPE_HOME,
									type);
							addOrganization(list, context,
									getJsonObj(json, "organizations"), values,
									rawContactId, Organization.TYPE_OTHER,
									type, null);
							if (getJsonObj(json, "company") != null
									&& getJsonObj(json, "organizations_work") == null) {
								addOrganization(list, context,
										getJsonObj(json, "company"), values,
										rawContactId, Organization.TYPE_WORK,
										type, "company");
							} else {
								addOrganization(list, context,
										getJsonObj(json, "organizations_work"),
										values, rawContactId,
										Organization.TYPE_WORK, type, null);
							}
							addOrganization(list, context,
									getJsonObj(json, "organizations_custom"),
									values, rawContactId,
									Organization.TYPE_CUSTOM, type, null);
							addNote(list, context, getJsonObj(json, "title"),
									values, rawContactId, type);
							// 往data表入nickname数据
							addNickname(list, context,
									getJsonObj(json, "nickname"), values,
									rawContactId, type);
							// 往data表入name数据
							addName(list, context, json, values, rawContactId,
									type);
							addWebsite(list, context, getJsonObj(json, "web"),
									values, rawContactId, Website.TYPE_OTHER,
									type);
							addWebsite(list, context,
									getJsonObj(json, "web_home"), values,
									rawContactId, Website.TYPE_HOME, type);
							addWebsite(list, context,
									getJsonObj(json, "web_work"), values,
									rawContactId, Website.TYPE_WORK, type);
							addWebsite(list, context,
									getJsonObj(json, "web_blog"), values,
									rawContactId, Website.TYPE_BLOG, type);
							addWebsite(list, context,
									getJsonObj(json, "web_ftp"), values,
									rawContactId, Website.TYPE_FTP, type);
							addWebsite(list, context,
									getJsonObj(json, "web_homepage"), values,
									rawContactId, Website.TYPE_HOMEPAGE, type);
							addWebsite(list, context,
									getJsonObj(json, "web_profile"), values,
									rawContactId, Website.TYPE_PROFILE, type);
							addWebsite(list, context,
									getJsonObj(json, "web_custom"), values,
									rawContactId, Website.TYPE_CUSTOM, type);
							addIM(list, context, getJsonObj(json, "im_home"),
									values, rawContactId, Im.TYPE_HOME, type);
							addIM(list, context, getJsonObj(json, "im_work"),
									values, rawContactId, Im.TYPE_WORK, type);
							addIM(list, context, getJsonObj(json, "im"),
									values, rawContactId, Im.TYPE_OTHER, type);
							addIM(list, context, getJsonObj(json, "im_cuntom"),
									values, rawContactId, Im.TYPE_CUSTOM, type);
							addDate(list, context,
									getJsonObj(json, "birthday"), values,
									rawContactId, Event.TYPE_BIRTHDAY, type);
							addDate(list, context,
									getJsonObj(json, "event_other"), values,
									rawContactId, Event.TYPE_OTHER, type);
							addDate(list, context,
									getJsonObj(json, "event_cuntom"), values,
									rawContactId, Event.TYPE_CUSTOM, type);
							addDate(list, context,
									getJsonObj(json, "event_anniversary"),
									values, rawContactId,
									Event.TYPE_ANNIVERSARY, type);
							addGroup(list, context, getJsonObj(json, "group"),
									values, rawContactId, type);
							try {
								handlerValues(list, context);
								count++;
							} catch (OperationApplicationException e) {
								// TODO Auto-generated catch block
								continue;
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list = null;
		values = null;
		return count;
	}

	// 删除联系人信息
	/**
	 * 
	 */
	public boolean handlerContactDelete(Context context, JSONObject json) {
		boolean flag = false;
		JSONArray jsonarray = null;
		try {
			jsonarray = json.getJSONArray("contacts");
			if (jsonarray != null && jsonarray.length() == 1) {
				String rawContactId = null;
				json = jsonarray.getJSONObject(0);
				rawContactId = (getJsonObj(json, "id") != null) ? json
						.get("id").toString() : "0";

				ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

				try {
					ops.add(ContentProviderOperation
							.newDelete(ContactsContract.RawContacts.CONTENT_URI)
							.withSelection(RawContacts.CONTACT_ID + "=?",
									new String[] { rawContactId }).build());
					context.getContentResolver().applyBatch(
							ContactsContract.AUTHORITY, ops);
					flag = true;
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					// Log.i("jindan","RemoteException="+e.getMessage());
					e.printStackTrace();
				} catch (OperationApplicationException e) {
					// TODO Auto-generated catch block
					// Log.i("jindan","OperationApplicationException="+e.getMessage());
					e.printStackTrace();
				} finally {
					ops = null;
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return flag;
		} finally {
			// try {
			// Thread.sleep(100);
			jsonarray = null;
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		return flag;

	}

	// 添加电话信息
	private static void addTelephone(ArrayList<ContentProviderOperation> list,
			Context context, Object num, ContentValues values,
			long rawContactId, int phoneType, int type) {
		values.clear();
		//
		// Log.i("info","addTelephone");
		if (num != null && num instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) num).length(); i++) {
				try {
					JSONObject num1 = ((JSONArray) num).getJSONObject(i);
					Builder op2 = null;
					if (type == 0) {
						op2 = ContentProviderOperation
								.newInsert(android.provider.ContactsContract.Data.CONTENT_URI);
						if (phoneType == Phone.TYPE_CUSTOM) {
							if (getJsonObj(num1, "name") != null)
								op2.withValue(Data.DATA3, num1.get("name"));
						}
						op2.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
								.withValue(Phone.NUMBER,
										num1.get("value").toString())
								.withValue(Phone.TYPE, phoneType)
								.withValue(
										android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
										rawContactId).build();
					} else {
						if (getJsonObj(num1, "id") == null)
							continue;
						op2 = ContentProviderOperation
								.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI);
						if (phoneType == Phone.TYPE_CUSTOM) {
							if (getJsonObj(num1, "name") != null)
								op2.withValue(Data.DATA3, num1.get("name"));
						}
						op2.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
								.withValue(Phone.NUMBER,
										num1.get("value").toString())
								.withValue(Phone.TYPE, phoneType)
								.withSelection(
										Data._ID + "=?",
										new String[] { getJsonObj(num1, "id")
												.toString() }).build();
					}

					list.add(op2.build());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 添加邮箱信息
	private static void addEmail(ArrayList<ContentProviderOperation> list,
			Context context, Object num, ContentValues values,
			long rawContactId, int emailType, int type) {
		values.clear();
		// Log.i("info","addEmail");
		if (num != null && num instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) num).length(); i++) {
				try {
					JSONObject num1 = ((JSONArray) num).getJSONObject(i);
					Builder op2 = null;
					if (type == 0) {
						// //Log.i("info","custom="+emailType+"="+num1.toString()+"="+Email.TYPE_CUSTOM);
						op2 = ContentProviderOperation
								.newInsert(android.provider.ContactsContract.Data.CONTENT_URI);
						if (emailType == Email.TYPE_CUSTOM) {
							if (getJsonObj(num1, "name") != null)
								op2.withValue(Data.DATA3, num1.get("name"));
						}
						op2.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
								.withValue(Email.DATA,
										num1.get("value").toString())
								.withValue(Email.TYPE, emailType)
								.withValue(
										android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
										rawContactId);
					} else {

						if (getJsonObj(num1, "id") == null)
							continue;
						op2 = ContentProviderOperation
								.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI);
						if (emailType == Email.TYPE_CUSTOM) {
							if (getJsonObj(num1, "name") != null) {
								op2.withValue(Data.DATA3, num1.get("name"));
							}
						}
						op2.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
								.withValue(Email.DATA,
										num1.get("value").toString())
								.withValue(Email.TYPE, emailType)
								.withSelection(
										Data._ID + "=?",
										new String[] { getJsonObj(num1, "id")
												.toString() });
					}

					list.add(op2.build());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 添加邮箱信息
	private static void addAddress(ArrayList<ContentProviderOperation> list,
			Context context, Object num, ContentValues values,
			long rawContactId, int addressType, int type) {
		values.clear();
		// Log.i("info","addAddress");
		// //Log.i("jindan","addAddress="+num+"");
		if (num != null && num instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) num).length(); i++) {
				try {
					JSONObject num1 = ((JSONArray) num).getJSONObject(i);
					Builder op2 = null;
					if (type == 0) {
						op2 = ContentProviderOperation
								.newInsert(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withValue(
										android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
										rawContactId);
						;

					} else {
						if (getJsonObj(num1, "id") == null)
							continue;
						op2 = ContentProviderOperation
								.newUpdate(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withSelection(
										Data._ID + "=?",
										new String[] { getJsonObj(num1, "id")
												.toString() });

					}
					op2.withValue(Data.MIMETYPE,
							StructuredPostal.CONTENT_ITEM_TYPE);
					if (addressType == StructuredPostal.TYPE_CUSTOM) {
						if (getJsonObj(num1, "name") != null) {
							op2.withValue(Data.DATA3, getJsonObj(num1, "name")
									.toString());
						}
					}
					if (getJsonObj(num1, "city") != null) {
						op2.withValue(StructuredPostal.CITY,
								getJsonObj(num1, "city").toString());
					}
					if (getJsonObj(num1, "country") != null) {
						op2.withValue(StructuredPostal.COUNTRY,
								getJsonObj(num1, "country").toString());
					}
					if (getJsonObj(num1, "region") != null) {
						op2.withValue(StructuredPostal.REGION,
								getJsonObj(num1, "region").toString());
					}
					if (getJsonObj(num1, "street") != null) {
						op2.withValue(StructuredPostal.STREET,
								getJsonObj(num1, "street").toString());
					}
					if (getJsonObj(num1, "postcode") != null) {
						op2.withValue(StructuredPostal.POSTCODE,
								getJsonObj(num1, "postcode").toString());
					}

					op2.withValue(StructuredPostal.TYPE, addressType);

					list.add(op2.build());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 添加组织信息
	private static void addOrganization(
			ArrayList<ContentProviderOperation> list, Context context,
			Object num, ContentValues values, long rawContactId,
			int organizationType, int type, String flagstr) {
		values.clear();
		// Log.i("info","addOrganization");
		// //Log.i("info","addOrganization");
		if (num != null && num instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) num).length(); i++) {

				try {
					JSONObject num1 = ((JSONArray) num).getJSONObject(i);
					Builder op2 = null;
					if (type == 0) {
						op2 = ContentProviderOperation
								.newInsert(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withValue(
										android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
										rawContactId);
					} else {
						if (getJsonObj(num1, "id") == null)
							continue;
						op2 = ContentProviderOperation
								.newUpdate(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withSelection(
										Data._ID + "=?",
										new String[] { getJsonObj(num1, "id")
												.toString() });
					}
					Object str = null;
					if ((str = getJsonObj(num1, "value")) != null
							&& !getJsonObj(num1, "value").toString().trim()
									.equals("")) {
						op2.withValue(Data.MIMETYPE,
								Organization.CONTENT_ITEM_TYPE);
						if (flagstr != null && flagstr.equals("company")) {
							op2.withValue(Organization.COMPANY,
									num1.get("value").toString());
						} else {
							if (getJsonObj(num1, "value") != null) {
								op2.withValue(Organization.COMPANY,
										num1.get("value").toString());
							}
							if (getJsonObj(num1, "dec") != null) {
								op2.withValue(Organization.JOB_DESCRIPTION,
										num1.get("dec").toString());
							}
							if (getJsonObj(num1, "department") != null) {
								op2.withValue(Organization.DEPARTMENT, num1
										.get("department").toString());
							}
							if (getJsonObj(num1, "title") != null) {
								op2.withValue(Organization.TITLE,
										num1.get("title").toString());
							}
							op2.withValue(Organization.TYPE, organizationType);
							if (organizationType == Organization.TYPE_CUSTOM) {
								if (getJsonObj(num1, "name") != null) {
									op2.withValue(Data.DATA3,
											getJsonObj(num1, "name").toString());
								}
							}
						}
						list.add(op2.build());
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 添加备注
	private static void addNote(ArrayList<ContentProviderOperation> list,
			Context context, Object note, ContentValues values,
			long rawContactId, int type) {
		values.clear();
		// Log.i("info","addNote");
		// //Log.i("jindan","addNote="+note+"");
		Builder op2 = null;

		if (note == null || !(note instanceof JSONArray)) {
			return;
		}
		JSONArray not = (JSONArray) note;
		if (not.length() <= 0)
			return;
		JSONObject json = null;
		try {
			json = (JSONObject) not.get(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type == 0) {
			op2 = ContentProviderOperation
					.newInsert(
							android.provider.ContactsContract.Data.CONTENT_URI)
					.withValue(
							android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
							rawContactId);
		} else {
			if (getJsonObj(json, "id") == null)
				return;
			op2 = ContentProviderOperation.newUpdate(
					android.provider.ContactsContract.Data.CONTENT_URI)
					.withSelection(Data._ID + "=?",
							new String[] { getJsonObj(json, "id").toString() });

		}
		op2.withValue(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE)
				.withValue(
						Note.NOTE,
						note == null ? "" : getJsonObj(json, "value")
								.toString()).build();
		list.add(op2.build());
	}

	// 添加昵称
	private static void addNickname(ArrayList<ContentProviderOperation> list,
			Context context, Object nickname, ContentValues values,
			long rawContactId, int type) {
		values.clear();
		// Log.i("info","addNickname");
		// //Log.i("jindan","addNickname="+nickname+"");
		Builder op2 = null;
		if (nickname == null || !(nickname instanceof JSONArray)) {
			return;
		}
		JSONArray nick = (JSONArray) nickname;
		if (nick.length() <= 0)
			return;
		JSONObject json = null;
		try {
			json = (JSONObject) nick.get(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (type == 0) {
			op2 = ContentProviderOperation
					.newInsert(
							android.provider.ContactsContract.Data.CONTENT_URI)
					.withValue(
							android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
							rawContactId);
		} else {
			if (getJsonObj(json, "id") == null)
				return;
			op2 = ContentProviderOperation.newUpdate(
					android.provider.ContactsContract.Data.CONTENT_URI)
					.withSelection(Data._ID + "=?",
							new String[] { getJsonObj(json, "id").toString() });
		}
		if (getJsonObj(json, "value") != null) {
			op2.withValue(Data.MIMETYPE, Nickname.CONTENT_ITEM_TYPE)
					.withValue(
							Nickname.NAME,
							nickname == null ? "" : getJsonObj(json, "value")
									.toString()).build();
			list.add(op2.build());
		}

	}

	// 添加名字
	private static void addName(ArrayList<ContentProviderOperation> list,
			Context context, Object name, ContentValues values,
			long rawContactId, int type) {
		values.clear();
		// Log.i("info","addName");
		// //Log.i("jindan","addNickname="+nickname+"");
		Builder op2 = null;
		if (name != null) {
			JSONObject json = (JSONObject) name;
			if (json == null)
				return;
			if (type == 0) {
				op2 = ContentProviderOperation
						.newInsert(
								android.provider.ContactsContract.Data.CONTENT_URI)
						.withValue(
								android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
								rawContactId);
			} else {
				if (getJsonObjForArray(json, "id") == null)
					return;
				op2 = ContentProviderOperation.newUpdate(
						android.provider.ContactsContract.Data.CONTENT_URI)
						.withSelection(
								Data._ID + "=?",
								new String[] { getJsonObjForArray(json,
										"nameid").toString() });
			}
			op2.withValue(
					Data.MIMETYPE,
					ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
			// //Log.i("tag",getJsonObj(json,"name")+"="+getJsonObj(json,"firstname")+"="+getJsonObj(json,"lastname")+"="+getJsonObj(json,"middlename"));
			if (getJsonObjForArray(json, "name") != null) {
				op2.withValue(StructuredName.DISPLAY_NAME,
						getJsonObjForArray(json, "name").toString());
			}
			if (getJsonObjForArray(json, "firstname") != null) {
				op2.withValue(StructuredName.GIVEN_NAME,
						getJsonObjForArray(json, "firstname").toString());
			}
			if (getJsonObjForArray(json, "lastname") != null) {
				op2.withValue(StructuredName.FAMILY_NAME,
						getJsonObjForArray(json, "lastname").toString());
			}
			if (getJsonObjForArray(json, "middlename") != null) {
				op2.withValue(StructuredName.MIDDLE_NAME,
						getJsonObjForArray(json, "middlename").toString());
			}
			list.add(op2.build());
		}
	}

	// 添加网址
	private static void addWebsite(ArrayList<ContentProviderOperation> list,
			Context context, Object num, ContentValues values,
			long rawContactId, int websiteType, int type) {
		values.clear();
		// Log.i("info","addWebsite");
		// //Log.i("jindan","addWebsite="+num+"");
		if (num != null && num instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) num).length(); i++) {
				try {
					JSONObject num1 = ((JSONArray) num).getJSONObject(i);
					Builder op2 = null;
					if (type == 0) {
						op2 = ContentProviderOperation
								.newInsert(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withValue(
										android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
										rawContactId);
					} else {
						if (getJsonObj(num1, "id") == null)
							continue;
						op2 = ContentProviderOperation
								.newUpdate(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withSelection(
										Data._ID + "=?",
										new String[] { getJsonObj(num1, "id")
												.toString() });
					}
					if (websiteType == Website.TYPE_CUSTOM) {
						if (getJsonObj(num1, "name") != null) {
							op2.withValue(Data.DATA3, getJsonObj(num1, "name")
									.toString());
						}
					}
					op2.withValue(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE)
							.withValue(Website.DATA,
									num1.get("value").toString())
							.withValue(Website.TYPE, websiteType).build();
					list.add(op2.build());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 添加网址
	private static void addIM(ArrayList<ContentProviderOperation> list,
			Context context, Object num, ContentValues values,
			long rawContactId, int imType, int type) {
		values.clear();
		// Log.i("info","addIM");
		if (num != null && num instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) num).length(); i++) {
				try {
					JSONObject num1 = ((JSONArray) num).getJSONObject(i);
					Builder op2 = null;
					if (type == 0) {
						op2 = ContentProviderOperation
								.newInsert(android.provider.ContactsContract.Data.CONTENT_URI);
						op2.withValue(
								android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
								rawContactId);
						op2.withValue(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
					} else {
						if (getJsonObj(num1, "id") == null)
							continue;
						op2 = ContentProviderOperation
								.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI);
						op2.withSelection(Data._ID + "=?",
								new String[] { getJsonObj(num1, "id")
										.toString() });
						op2.withValue(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
					}
					if (num1.get("value") != null) {
						op2.withValue(Im.DATA, num1.get("value").toString());
					}
					if (num1.get("protocol") != null) {
						op2.withValue(Im.PROTOCOL, num1.get("protocol")
								.toString());
					}
					if (imType == Im.TYPE_CUSTOM) {
						if (getJsonObj(num1, "name") != null) {
							op2.withValue(Data.DATA3, getJsonObj(num1, "name")
									.toString());
						}
					}
					op2.withValue(Im.TYPE, imType);
					list.add(op2.build());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 添加日期
	private static void addDate(ArrayList<ContentProviderOperation> list,
			Context context, Object num, ContentValues values,
			long rawContactId, int dateType, int type) {
		values.clear();
		// Log.i("info","addDate");
		// //Log.i("jindan","addDate="+num+"");
		if (num != null && num instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) num).length(); i++) {
				try {
					JSONObject num1 = ((JSONArray) num).getJSONObject(i);
					Builder op2 = null;
					if (type == 0) {
						op2 = ContentProviderOperation
								.newInsert(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withValue(
										android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
										rawContactId);
					} else {
						if (getJsonObj(num1, "id") == null)
							continue;
						op2 = ContentProviderOperation
								.newUpdate(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withSelection(
										Data._ID + "=?",
										new String[] { getJsonObj(num1, "id")
												.toString() });
					}
					if (dateType == Event.TYPE_CUSTOM) {
						if (getJsonObj(num1, "name") != null) {
							op2.withValue(Data.DATA3, getJsonObj(num1, "name")
									.toString());
						}
					}
					if (getJsonObj(num1, "value") != null) {
						op2.withValue(Data.MIMETYPE, Event.CONTENT_ITEM_TYPE)
								.withValue(Event.DATA,
										getJsonObj(num1, "value").toString())
								.withValue(Event.TYPE, dateType).build();
						list.add(op2.build());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 添加日期
	private static void addGroup(ArrayList<ContentProviderOperation> list,
			Context context, Object num, ContentValues values,
			long rawContactId, int type) {
		values.clear();
		// Log.i("info","addGroup");
		// //Log.i("jindan","addGroup="+num+"");
		if (num != null && num instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) num).length(); i++) {
				try {
					JSONObject num1 = ((JSONArray) num).getJSONObject(i);
					Builder op2 = null;
					if (type == 0) {
						op2 = ContentProviderOperation
								.newInsert(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withValue(
										android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
										rawContactId);
					} else {
						if (getJsonObj(num1, "id") == null)
							continue;
						op2 = ContentProviderOperation
								.newUpdate(
										android.provider.ContactsContract.Data.CONTENT_URI)
								.withSelection(
										Data._ID + "=?",
										new String[] { getJsonObj(num1, "id")
												.toString() });
					}
					if (getJsonObj(num1, "value") != null) {
						op2.withValue(Data.MIMETYPE,
								GroupMembership.CONTENT_ITEM_TYPE)
								.withValue(Event.DATA,
										getJsonObj(num1, "value").toString())
								.build();
						list.add(op2.build());
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static synchronized void handlerValues(
			ArrayList<ContentProviderOperation> list, Context context)
			throws OperationApplicationException {
		if (list != null && list.size() > 0) {
			try {
				context.getContentResolver().applyBatch(
						ContactsContract.AUTHORITY, list);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		list.clear();
	}

	public static boolean getPhoneContactsById(Context context,
			long rawContactId) {
		String[] PHONES_PROJECTION = new String[] { ContactsContract.Contacts._ID };
		ContentResolver resolver = context.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(
				ContactsContract.Contacts.CONTENT_URI, PHONES_PROJECTION,
				ContactsContract.Contacts._ID + " = ? ",
				new String[] { rawContactId + "" }, null);
		if (phoneCursor != null && phoneCursor.moveToFirst()) {
			// 得到id
			String phoneid = phoneCursor.getString(0);
			phoneCursor.close();
			return true;
		}
		return false;
	}

}
