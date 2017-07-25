package com.digitaldan.jomnilinkII;

/**
*  Copyright (C) 2009  Dan Cunningham
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation, version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.digitaldan.jomnilinkII.MessageTypes.Acknowledge;
import com.digitaldan.jomnilinkII.MessageTypes.ActivateKeypadEmergency;
import com.digitaldan.jomnilinkII.MessageTypes.AudioSourceStatus;
import com.digitaldan.jomnilinkII.MessageTypes.CommandMessage;
import com.digitaldan.jomnilinkII.MessageTypes.ConnectedSecurityCommand;
import com.digitaldan.jomnilinkII.MessageTypes.ConnectedSecurityStatus;
import com.digitaldan.jomnilinkII.MessageTypes.DownloadNames;
import com.digitaldan.jomnilinkII.MessageTypes.EnableNotifications;
import com.digitaldan.jomnilinkII.MessageTypes.EndOfData;
import com.digitaldan.jomnilinkII.MessageTypes.EventLogData;
import com.digitaldan.jomnilinkII.MessageTypes.ExtendedObjectStatus;
import com.digitaldan.jomnilinkII.MessageTypes.NameData;
import com.digitaldan.jomnilinkII.MessageTypes.NegativeAcknowledge;
import com.digitaldan.jomnilinkII.MessageTypes.ObjectProperties;
import com.digitaldan.jomnilinkII.MessageTypes.ObjectStatus;
import com.digitaldan.jomnilinkII.MessageTypes.ObjectTypeCapacities;
import com.digitaldan.jomnilinkII.MessageTypes.OtherEventNotifications;
import com.digitaldan.jomnilinkII.MessageTypes.ReqAudioSourceStatus;
import com.digitaldan.jomnilinkII.MessageTypes.ReqObjectProperties;
import com.digitaldan.jomnilinkII.MessageTypes.ReqObjectStatus;
import com.digitaldan.jomnilinkII.MessageTypes.ReqObjectTypeCapacities;
import com.digitaldan.jomnilinkII.MessageTypes.ReqSecurityCodeValidation;
import com.digitaldan.jomnilinkII.MessageTypes.SecurityCodeValidation;
import com.digitaldan.jomnilinkII.MessageTypes.SetTimeCommand;
import com.digitaldan.jomnilinkII.MessageTypes.SystemFeatures;
import com.digitaldan.jomnilinkII.MessageTypes.SystemFormats;
import com.digitaldan.jomnilinkII.MessageTypes.SystemInformation;
import com.digitaldan.jomnilinkII.MessageTypes.SystemStatus;
import com.digitaldan.jomnilinkII.MessageTypes.SystemTroubles;
import com.digitaldan.jomnilinkII.MessageTypes.UploadEventRecord;
import com.digitaldan.jomnilinkII.MessageTypes.UploadNames;
import com.digitaldan.jomnilinkII.MessageTypes.ZoneReadyStatus;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AreaProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AudioSourceProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AudioZoneProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AuxSensorProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ButtonProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.CodeProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.MessageProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ThermostatProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.UnitProperties;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ZoneProperties;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.AccessControlReaderLockStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.AccessControlReaderStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.AreaStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.AudioZoneStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.AuxSensorStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.ExpansionStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.ExtendedThermostatStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.MessageStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.Status;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.ThermostatStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.UnitStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.UserSettingStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.ZoneStatus;

public class MessageFactory {

	public static Message fromBytes(byte[] bytes) throws IOException, OmniUnknownMessageTypeException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));

		int start = in.readUnsignedByte();
		int length = in.readUnsignedByte();
		int type = in.readUnsignedByte();

		int len = length - 1;
		byte[] data = new byte[len];
		in.readFully(data);
		bis = new ByteArrayInputStream(data);
		in = new DataInputStream(bis);

		switch (type) {
		case Message.MESG_TYPE_ACK:
			return acknowledge();
		case Message.MESG_TYPE_NEG_ACK:
			return negativeAcknowledge();
		case Message.MESG_TYPE_END_OF_DATA:
			return endOfData();
		case Message.MESG_TYPE_SYS_INFO:
			return systemInformation(in);
		case Message.MESG_TYPE_SYS_STATUS:
			return systemStatus(in, len);
		case Message.MESG_TYPE_SYS_TROUBLES:
			return systemTroubles(in, len);
		case Message.MESG_TYPE_SYS_FEATURES:
			return systemFeatures(in, len);
		case Message.MESG_TYPE_SYS_FORMATS:
			return systemFormats(in, len);
		case Message.MESG_TYPE_OBJ_CAPACITY:
			return objectTypeCapacities(in, len);
		case Message.MESG_TYPE_OBJ_PROP:
			return objectProperties(in, len);
		case Message.MESG_TYPE_OBJ_STATUS:
			return objectStatus(in, len, false);
		case Message.MESG_TYPE_EXT_OBJ_STATUS:
			return objectStatus(in, len, true);
		case Message.MESG_TYPE_AUDIO_SOURCE_STATUS:
			return audioSourceStatus(in, len);
		case Message.MESG_TYPE_ZONE_READY:
			return zoneReadyStatus(in, len);
		case Message.MESG_TYPE_OTHER_EVENT_NOTIFY:
			return otherEventNOtification(in, len);
		case Message.MESG_TYPE_EVENT_LOG_DATA:
			return eventLogData(in, len);
		case Message.MESG_TYPE_NAME_DATA:
			return nameData(in, len);
		case Message.MESG_TYPE_SEC_CODE_VALID:
			return securityCodeValidation(in, len);
		default:
			throw new OmniUnknownMessageTypeException(type);
		}
	}

	public static byte[] toBytes(Message msg) throws IOException, OmniUnknownMessageTypeException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(bos);
		os.writeByte(msg.getMessageType());

		switch (msg.getMessageType()) {
		case Message.MESG_TYPE_REQ_SYS_INFO:
		case Message.MESG_TYPE_REQ_SYS_STATUS:
		case Message.MESG_TYPE_REQ_SYS_TROUBLES:
		case Message.MESG_TYPE_REQ_SYS_FEATURES:
		case Message.MESG_TYPE_REQ_SYS_FORMATS:
		case Message.MESG_TYPE_CLEAR_NAMES:
		case Message.MESG_TYPE_CLEAR_VOICES:
			break;
		case Message.MESG_TYPE_ENABLE_NOTIFICATIONS: {
			os.writeBoolean(((EnableNotifications) msg).isEnabled());
		}
			break;
		case Message.MESG_TYPE_REQ_OBJ_CAPACITY: {
			ReqObjectTypeCapacities m = (ReqObjectTypeCapacities) msg;
			os.writeByte(m.objectType());
		}
			break;
		case Message.MESG_TYPE_REQ_OBJ_PROP: {
			ReqObjectProperties m = (ReqObjectProperties) msg;
			os.writeByte(m.objectType());
			os.writeShort(m.objectNum());
			os.writeByte(m.direction());
			os.writeByte(m.filter1());
			os.writeByte(m.filter2());
			os.writeByte(m.filter3());
		}
			break;
		case Message.MESG_TYPE_REQ_EXT_OBJ_STATUS:
		case Message.MESG_TYPE_REQ_OBJ_STATUS: {
			ReqObjectStatus m = (ReqObjectStatus) msg;
			os.writeByte(m.objectType());
			os.writeShort(m.objectStart());
			os.writeShort(m.objectEnd());
		}
			break;
		case Message.MESG_TYPE_REQ_AUDIO_SOURCE_STATUS: {
			ReqAudioSourceStatus m = (ReqAudioSourceStatus) msg;
			os.writeShort(m.source());
			os.writeByte(m.position());
		}
			break;
		case Message.MESG_TYPE_REQ_ZONE_READY:
		case Message.MESG_TYPE_REQ_CONN_SEC_STATUS:
			break;
		case Message.MESG_TYPE_COMMAND: {
			CommandMessage m = (CommandMessage) msg;
			os.writeByte(m.getCommand());
			os.writeByte(m.getParameter1());
			os.writeShort(m.getParameter2());
		}
			break;
		case Message.MESG_TYPE_UPLOAD_EVENT_LOG: {
			UploadEventRecord m = (UploadEventRecord) msg;
			os.writeShort(m.getEventNumber());
			os.writeByte(m.getDirection());
		}
			break;
		case Message.MESG_TYPE_UPLOAD_NAMES: {
			UploadNames m = (UploadNames) msg;
			os.writeByte(m.getObjectType());
			os.writeShort(m.getObjectNumber());
		}
			break;
		case Message.MESG_TYPE_CONN_SEC_COMMAND: {
			ConnectedSecurityCommand m = (ConnectedSecurityCommand) msg;
			os.writeByte(m.getCommand());
			os.writeByte(m.getPartition());
			os.writeByte(m.getDigit1());
			os.writeByte(m.getDigit2());
			os.writeByte(m.getDigit3());
			os.writeByte(m.getDigit4());
			os.writeByte(m.getDigit5());
			os.writeByte(m.getDigit6());
		}
			break;
		case Message.MESG_TYPE_SET_TIME: {
			SetTimeCommand m = (SetTimeCommand) msg;
			os.writeByte(m.getYear());
			os.writeByte(m.getMonth());
			os.writeByte(m.getDay());
			os.writeByte(m.getDayOfWeek());
			os.writeByte(m.getHour());
			os.writeByte(m.getMinute());
			os.writeBoolean(m.isDaylightSavings());
		}
			break;
		case Message.MESG_TYPE_ACT_KEYPAD_EMERGENCY: {
			ActivateKeypadEmergency m = (ActivateKeypadEmergency) msg;
			os.writeByte(m.getArea());
			os.writeByte(m.getEmergencyType());
		}
			break;
		case Message.MESG_TYPE_REQ_SEC_CODE_VALID: {
			ReqSecurityCodeValidation m = (ReqSecurityCodeValidation) msg;
			os.writeByte(m.getArea());
			os.writeByte(m.getDigit1());
			os.writeByte(m.getDigit2());
			os.writeByte(m.getDigit3());
			os.writeByte(m.getDigit4());
		}
			break;
		case Message.MESG_TYPE_DOWNLOAD_NAMES: {
			DownloadNames m = (DownloadNames) msg;
			int t = m.getObjectType();
			os.writeByte(t);
			os.writeShort(m.getObjectNumber());
			byte[] d;
			if (t == Message.OBJ_TYPE_ZONE || t == Message.OBJ_TYPE_MESG || t == Message.OBJ_TYPE_AUX_SENSOR) {
				d = new byte[15];
			} else {
				d = new byte[12];
			}
			byte[] s = m.getName().getBytes();
			int cnt = s.length;
			if (s.length > d.length) {
				cnt = d.length;
			}
			System.arraycopy(s, 0, d, 0, cnt);
			os.write(d);
		}
			break;
		default:
			throw new OmniUnknownMessageTypeException(msg.getMessageType());
		}
		byte[] data = bos.toByteArray();
		bos.reset();

		os.writeByte(Message.MESG_START);
		os.writeByte(data.length);
		os.write(data);

		//calc crc bytes of data, type and length
		byte[] crcBytes = new byte[data.length + 1];
		System.arraycopy(data, 0, crcBytes, 1, data.length);
		crcBytes[0] = (byte) data.length;
		int crc = MessageUtils.crc16(crcBytes);

		//LSB then MSB
		os.writeByte((byte) crc);
		os.writeByte((byte) (crc >> 8));

		return bos.toByteArray();
	}

	protected static Acknowledge acknowledge() {
		return new Acknowledge();
	}

	protected static NegativeAcknowledge negativeAcknowledge() {
		return new NegativeAcknowledge();
	}

	protected static EndOfData endOfData() {
		return new EndOfData();
	}

	protected static SystemInformation systemInformation(DataInputStream in) throws IOException {
		int model = in.readUnsignedByte();
		int major = in.readUnsignedByte();
		int minor = in.readUnsignedByte();
		int revision = in.readUnsignedByte();
		byte[] phoneBytes = new byte[15];
		in.readFully(phoneBytes);
		String phone = new String(phoneBytes);
		return new SystemInformation(model, major, minor, revision, phone);
	}

	protected static SystemStatus systemStatus(DataInputStream in, int len) throws IOException {
		boolean timeDateValid = in.readBoolean();
		int year = in.readUnsignedByte();
		int month = in.readUnsignedByte();
		int day = in.readUnsignedByte();
		int dayOfWeek = in.readUnsignedByte();
		int hour = in.readUnsignedByte();
		int minute = in.readUnsignedByte();
		int second = in.readUnsignedByte();
		boolean daylightSavings = in.readBoolean();
		int sunriseHour = in.readUnsignedByte();
		int sunriseMinute = in.readUnsignedByte();
		int sunsetHour = in.readUnsignedByte();
		int sunsetMinute = in.readUnsignedByte();
		int batteryReading = in.readUnsignedByte();
		HashMap<Integer, Integer> alarms = new HashMap<>();
		for (int i = 16; i < len; i = i + 2) {
			alarms.put(new Integer(in.readUnsignedByte()), new Integer(in.readUnsignedByte()));
		}
		return new SystemStatus(timeDateValid, year, month, day, dayOfWeek, hour, minute, second, daylightSavings,
				sunriseHour, sunriseMinute, sunsetHour, sunsetMinute, batteryReading, alarms);
	}

	protected static SystemTroubles systemTroubles(DataInputStream in, int length) throws IOException {
		int[] troubles = new int[length];
		for (int i = 0; i < length; i++) {
			troubles[i] = in.readUnsignedByte();
		}
		return new SystemTroubles(troubles);
	}

	protected static SystemFeatures systemFeatures(DataInputStream in, int length) throws IOException {
		int[] features = new int[length];
		for (int i = 0; i < length; i++) {
			features[i] = in.readUnsignedByte();
		}
		return new SystemFeatures(features);
	}

	protected static SystemFormats systemFormats(DataInputStream in, int length) throws IOException {
		int tempFormat = in.readUnsignedByte();
		int timeformat = in.readUnsignedByte();
		int dateFormat = in.readUnsignedByte();
		return new SystemFormats(tempFormat, timeformat, dateFormat);
	}

	protected static ObjectTypeCapacities objectTypeCapacities(DataInputStream in, int length) throws IOException {
		int objectType = in.readUnsignedByte();
		int capacity = in.readUnsignedShort();
		return new ObjectTypeCapacities(objectType, capacity);
	}

	protected static ObjectStatus objectStatus(DataInputStream in, int length, boolean extended) throws IOException {
		int statusType = in.readUnsignedByte();

		int recordLength = 0;
		if (extended) {
			recordLength = in.readUnsignedByte();
		}

		Status[] status;
		switch (statusType) {
		case Message.OBJ_TYPE_ZONE: {
			status = new ZoneStatus[(length - 1) / 4];
			for (int i = 0; i < status.length; i++) {
				status[i] = ZoneStatus.builder()
							.number(in.readUnsignedShort())
							.status(in.readUnsignedByte())
							.loop(in.readUnsignedByte())
							.build();
			}
		}
			break;
		case Message.OBJ_TYPE_UNIT: {
			status = new UnitStatus[(length - 1) / 5];
			for (int i = 0; i < status.length; i++) {
				status[i] = UnitStatus.builder()
							.number(in.readUnsignedShort())
							.status(in.readUnsignedByte())
							.time(in.readUnsignedShort())
							.build();
			}
		}
			break;
		case Message.OBJ_TYPE_AREA: {
			status = new AreaStatus[(length - 1) / 6];
			for (int i = 0; i < status.length; i++) {
				status[i] = AreaStatus.builder()
						.number(in.readUnsignedShort())
						.mode(in.readUnsignedByte())
						.alarms(in.readUnsignedByte())
						.entryTimer(in.readUnsignedByte())
						.exitTimer(in.readUnsignedByte()).build();
			}
		}
			break;
		case Message.OBJ_TYPE_THERMO: {
			if (!extended) {
				status = new ThermostatStatus[(length - 1) / 9];
				for (int i = 0; i < status.length; i++) {
					status[i] =
							ThermostatStatus.builder()
							.number(in.readUnsignedShort() )
							.status(in.readUnsignedByte())
							.temperature(in.readUnsignedByte() )
							.heatSetpoint(in.readUnsignedByte() )
							.coolSetpoint(in.readUnsignedByte() )
							.mode(in.readUnsignedByte())
							.fan(in.readUnsignedByte() )
							.hold(in.readUnsignedByte())
							.build();
				}
			} else {
				status = new ExtendedThermostatStatus[(length - 1) / 14];
				for (int i = 0; i < status.length; i++) {
					status[i] = ExtendedThermostatStatus.builder()
								.number(in.readUnsignedShort())
								.status( in.readUnsignedByte())
								.temperature(in.readUnsignedByte())
								.heatSetpoint(in.readUnsignedByte())
								.coolSetpoint(in.readUnsignedByte())
								.mode(in.readUnsignedByte())
								.fan(in.readUnsignedByte())
								.humidity(in.readUnsignedByte())
								.humiditySetpoint(in.readUnsignedByte())
								.dehumidifySetpoint(in.readUnsignedByte())
								.outdoorTemp(in.readUnsignedByte())
								.extendedStatus(in.readUnsignedByte())
								.build();
				}
			}
		}
			break;
		case Message.OBJ_TYPE_MESG: {
			status = new MessageStatus[(length - 1) / 3];
			for (int i = 0; i < status.length; i++) {
				status[i] = MessageStatus.builder()
							.number(in.readUnsignedShort())
							.status(in.readUnsignedByte())
							.build();
			}
		}
			break;
		case Message.OBJ_TYPE_AUX_SENSOR: {
			status = new AuxSensorStatus[(length - 1) / 6];
			for (int i = 0; i < status.length; i++) {
				status[i] = AuxSensorStatus.builder()
							.number(in.readUnsignedShort())
							.status(in.readUnsignedByte())
							.temp(in.readUnsignedByte())
							.heatSetpoint(in.readUnsignedByte())
							.coolSetpoint(in.readUnsignedByte())
							.build();
			}
		}
			break;
		case Message.OBJ_TYPE_AUDIO_ZONE: {
			status = new AudioZoneStatus[(length - 1) / 6];
			for (int i = 0; i < status.length; i++) {
				status[i] = AudioZoneStatus.builder()
							.number(in.readUnsignedShort())
							.power(in.readBoolean())
							.source(in.readUnsignedByte())
							.volume(in.readUnsignedByte())
							.mute(in.readBoolean())
							.build();
			}
		}
			break;
		case Message.OBJ_TYPE_EXP: {
			status = new ExpansionStatus[(length - 1) / 4];
			for (int i = 0; i < status.length; i++) {
				status[i] = ExpansionStatus.builder()
							.number(in.readUnsignedShort())
							.status(in.readUnsignedByte())
							.battery(in.readUnsignedByte())
							.build();
			}
		}
			break;
		case Message.OBJ_TYPE_USER_SETTING: {
			status = new UserSettingStatus[(length - 1) / 4];
			for (int i = 0; i < status.length; i++) {
				status[i] = UserSettingStatus.builder()
							.number(in.readUnsignedShort())
							.settingType(in.readUnsignedByte())
							.settingValue(in.readUnsignedByte())
							.build();
			}
		}
			break;
		case Message.OBJ_TYPE_CONTROL_READER: {
			status = new AccessControlReaderStatus[(length - 1) / 5];
			for (int i = 0; i < status.length; i++) {
				status[i] = AccessControlReaderStatus.builder()
							.number(in.readUnsignedShort())
							.granted(in.readBoolean())
							.lastUser(in.readUnsignedShort())
							.build();
			}
		}
			break;
		case Message.OBJ_TYPE_CONTROL_LOCK: {
			status = new AccessControlReaderLockStatus[(length - 1) / 5];
			for (int i = 0; i < status.length; i++) {
				status[i] = AccessControlReaderLockStatus.builder()
                        .number(in.readUnsignedShort())
                        .locked(in.readBoolean())
                        .timer(in.readUnsignedShort())
                        .build();

			}
		}
			break;
		default:
			throw new IOException("Unknown status type " + statusType);
		}
		if (extended) {
			return new ExtendedObjectStatus(statusType, recordLength, status);
		} else {
			return new ObjectStatus(statusType, status);
		}
	}

	private static String readName(byte[] nameBytes) {
		String name = new String(nameBytes);
		if (name.indexOf('\0') >= 0) {
			name = name.substring(0, name.indexOf('\0'));
		}
		return name;
	}

	protected static ObjectProperties objectProperties(DataInputStream in, int length) throws IOException {
		int objectType = in.readUnsignedByte();
		int number = in.readUnsignedShort();
		byte[] nameShort = new byte[12];
		byte[] nameLong = new byte[15];
		switch (objectType) {
		case Message.OBJ_TYPE_ZONE: {
			int status = in.readUnsignedByte();
			int loop = in.readUnsignedByte();
			int type = in.readUnsignedByte();
			int area = in.readUnsignedByte();
			int options = in.readUnsignedByte();
			in.readFully(nameLong);
			String name = readName(nameLong);
			return ZoneProperties.builder()
					.number(number)
					.status(status)
					.loop(loop)
					.zoneType(type)
					.area(area)
					.options(options)
					.name(name)
                    .build();
		}
		case Message.OBJ_TYPE_UNIT: {
			int state = in.readUnsignedByte();
			int time = in.readUnsignedShort();
			int type = in.readUnsignedByte();
			in.readFully(nameShort);
			String name = readName(nameShort);
			return UnitProperties.builder()
					.number(number)
					.state(state)
					.time(time)
					.unitType(type)
					.name(name)
					.build();
		}
		case Message.OBJ_TYPE_BUTTON: {
			in.readFully(nameShort);
			String name = readName(nameShort);
			return ButtonProperties.builder().number(number).name(name).build();
		}
		case Message.OBJ_TYPE_CODE: {
			in.readFully(nameShort);
			String name = readName(nameShort);
			return CodeProperties.builder().number(number).name(name).build();
		}
		case Message.OBJ_TYPE_AREA: {
			int mode = in.readUnsignedByte();
			int alarms = in.readUnsignedByte();
			int entryTimer = in.readUnsignedByte();
			int exitTimer = in.readUnsignedByte();
			boolean enabled = in.readBoolean();
			int exitDelay = in.readUnsignedByte();
			int entryDelay = in.readUnsignedByte();
			in.readFully(nameShort);
			String name = readName(nameShort);
			return AreaProperties.builder()
					.number(number)
					.mode(mode)
					.alarms(alarms)
					.entryTimer(entryTimer)
					.exitTimer(exitTimer)
					.enabled(enabled)
					.exitDelay(exitDelay)
					.entryDelay(entryDelay)
					.name(name)
					.build();
		}
		case Message.OBJ_TYPE_THERMO: {
			int status = in.readUnsignedByte();
			int temperature = in.readUnsignedByte();
			int heatSetpoint = in.readUnsignedByte();
			int coolSetpoint = in.readUnsignedByte();
			int mode = in.readUnsignedByte();
			int fan = in.readUnsignedByte();
			int hold = in.readUnsignedByte();
			int thermostatType = in.readUnsignedByte();
			in.readFully(nameShort);
			String name = readName(nameShort);
			return ThermostatProperties.builder()
					.number(number)
					.status(status)
					.temperature(temperature)
					.heatSetpoint(heatSetpoint)
					.coolSetpoint(coolSetpoint)
					.mode(mode)
					.fan(fan)
					.hold(hold)
                    .build();
		}
		case Message.OBJ_TYPE_MESG: {
			in.readFully(nameLong);
			String name = readName(nameLong);
			return MessageProperties.builder().number(number).name(name).build();
		}
		case Message.OBJ_TYPE_AUX_SENSOR: {
			int status = in.readUnsignedByte();
			int current = in.readUnsignedByte();
			int lowSetpoint = in.readUnsignedByte();
			int highSetpoint = in.readUnsignedByte();
			int sensorType = in.readUnsignedByte();
			in.readFully(nameLong);
			String name = readName(nameLong);
			return AuxSensorProperties.builder()
					.number(number)
					.status(status)
					.current(current)
					.lowSetpoint(lowSetpoint)
					.highSetpoint(highSetpoint)
					.sensorType(sensorType)
					.name(name)
					.build();
		}
		case Message.OBJ_TYPE_AUDIO_SOURCE: {
			in.readFully(nameShort);
			String name = readName(nameShort);
			return AudioSourceProperties.builder().number(number).name(name).build();
		}
		case Message.OBJ_TYPE_AUDIO_ZONE: {
			boolean on = in.readBoolean();
			int source = in.readUnsignedByte();
			int volume = in.readUnsignedByte();
			boolean mute = in.readBoolean();
			in.readFully(nameShort);
			String name = readName(nameShort);
			return AudioZoneProperties.builder()
					.number(number)
					.on(on)
					.source(source)
					.volume(volume)
					.mute(mute)
					.name(name)
					.build();
		}
		default:
			throw new IOException("Unknown property type " + objectType);
		}
	}

	protected static AudioSourceStatus audioSourceStatus(DataInputStream in, int length) throws IOException {
		int srcNumber = in.readUnsignedShort();
		int seqNumber = in.readUnsignedByte();
		int pos = in.readUnsignedByte();
		int fieldId = in.readUnsignedByte();
		byte[] data = new byte[length - 5];
		in.readFully(data);
		String sourceData = new String(data);
		return new AudioSourceStatus(srcNumber, seqNumber, pos, fieldId, sourceData);
	}

	protected static ZoneReadyStatus zoneReadyStatus(DataInputStream in, int length) throws IOException {
		int[] zones = new int[length];
		for (int i = 0; i < length; i++) {
			zones[i] = in.readUnsignedByte();
		}
		return new ZoneReadyStatus(zones);
	}

	protected static ConnectedSecurityStatus connectedSecurityStatus(DataInputStream in, int length)
			throws IOException {
		int[] parts = new int[length];
		for (int i = 0; i < length; i++) {
			parts[i] = in.readUnsignedByte();
		}
		return new ConnectedSecurityStatus(parts);
	}

	protected static OtherEventNotifications otherEventNOtification(DataInputStream in, int length) throws IOException {
		int[] notifications = new int[length / 2];
		for (int i = 0; i < notifications.length; i++) {
			notifications[i] = in.readUnsignedShort();
		}
		return new OtherEventNotifications(notifications);
	}

	protected static EventLogData eventLogData(DataInputStream in, int length) throws IOException {
		int eventNumber = in.readUnsignedShort();
		boolean timeDataValid = in.readBoolean();
		int month = in.readUnsignedByte();
		int day = in.readUnsignedByte();
		int hour = in.readUnsignedByte();
		int minute = in.readUnsignedByte();
		int eventType = in.readUnsignedByte();
		int parameter1 = in.readUnsignedByte();
		int parameter2 = in.readUnsignedShort();
		return new EventLogData(eventNumber, timeDataValid, month, day, hour, minute, eventType, parameter1,
				parameter2);
	}

	protected static NameData nameData(DataInputStream in, int length) throws IOException {

		int objectType = in.readUnsignedByte();
		int objectNumber = in.readUnsignedShort();
		byte[] data = new byte[length - 3];
		in.readFully(data);
		String name = new String(data);
		return new NameData(objectType, objectNumber, name);
	}

	protected static SecurityCodeValidation securityCodeValidation(DataInputStream in, int length) throws IOException {
		int code = in.readUnsignedByte();
		int level = in.readUnsignedByte();
		return new SecurityCodeValidation(code, level);
	}
}
