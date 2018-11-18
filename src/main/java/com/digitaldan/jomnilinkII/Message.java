
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

package com.digitaldan.jomnilinkII;

public interface Message {
	int MESG_START = 0x21;
	int MESG_TYPE_ACK = 0x01;
	int MESG_TYPE_NEG_ACK = 0x02;
	int MESG_TYPE_END_OF_DATA = 0x03;
	int MESG_TYPE_CLEAR_NAMES = 0x0B;
	int MESG_TYPE_DOWNLOAD_NAMES = 0x0C;
	int MESG_TYPE_UPLOAD_NAMES = 0x0D;
	int MESG_TYPE_NAME_DATA = 0x0E;
	int MESG_TYPE_CLEAR_VOICES = 0x0F;
	int MESG_TYPE_DOWNLOAD_VOICES = 0x10;
	int MESG_TYPE_UPLOAD_VOICES = 0x11;
	int MESG_TYPE_VOICE_DATA = 0x12;
	int MESG_TYPE_SET_TIME = 0x13;
	int MESG_TYPE_COMMAND = 0x14;
	int MESG_TYPE_ENABLE_NOTIFICATIONS = 0x15;
	int MESG_TYPE_REQ_SYS_INFO = 0x16;
	int MESG_TYPE_SYS_INFO = 0x17;
	int MESG_TYPE_REQ_SYS_STATUS = 0x18;
	int MESG_TYPE_SYS_STATUS = 0x19;
	int MESG_TYPE_REQ_SYS_TROUBLES = 0x1A;
	int MESG_TYPE_SYS_TROUBLES = 0x1B;
	int MESG_TYPE_REQ_SYS_FEATURES = 0x1C;
	int MESG_TYPE_SYS_FEATURES = 0x1D;
	int MESG_TYPE_REQ_OBJ_CAPACITY = 0x1E;
	int MESG_TYPE_OBJ_CAPACITY = 0x1F;
	int MESG_TYPE_REQ_OBJ_PROP = 0x20;
	int MESG_TYPE_OBJ_PROP = 0x21;
	int MESG_TYPE_REQ_OBJ_STATUS = 0x22;
	int MESG_TYPE_OBJ_STATUS = 0x23;
	int MESG_TYPE_UPLOAD_EVENT_LOG = 0x24;
	int MESG_TYPE_EVENT_LOG_DATA = 0x25;
	int MESG_TYPE_REQ_SEC_CODE_VALID = 0x26;
	int MESG_TYPE_SEC_CODE_VALID = 0x27;
	int MESG_TYPE_REQ_SYS_FORMATS = 0x28;
	int MESG_TYPE_SYS_FORMATS = 0x29;
	int MESG_TYPE_ACT_KEYPAD_EMERGENCY = 0x2C;
	int MESG_TYPE_REQ_CONN_SEC_STATUS = 0x2D;
	int MESG_TYPE_CONN_SEC_STATUS = 0x2E;
	int MESG_TYPE_CONN_SEC_COMMAND = 0x2F;
	int MESG_TYPE_REQ_AUDIO_SOURCE_STATUS = 0x30;
	int MESG_TYPE_AUDIO_SOURCE_STATUS = 0x31;
	int MESG_TYPE_REQ_EXT_OBJ_STATUS = 0x3A;
	int MESG_TYPE_EXT_OBJ_STATUS = 0x3B;
	int MESG_TYPE_OTHER_EVENT_NOTIFY = 0x37;
	int MESG_TYPE_REQ_ZONE_READY = 0x38;
	int MESG_TYPE_ZONE_READY = 0x39;

	int OBJ_TYPE_ZONE = 0x01;
	int OBJ_TYPE_UNIT = 0x02;
	int OBJ_TYPE_BUTTON = 0x03;
	int OBJ_TYPE_CODE = 0x04;
	int OBJ_TYPE_AREA = 0x05;
	int OBJ_TYPE_THERMO = 0x06;
	int OBJ_TYPE_MESG = 0x07;
	int OBJ_TYPE_AUX_SENSOR = 0x08;
	int OBJ_TYPE_AUDIO_SOURCE = 0x09;
	int OBJ_TYPE_AUDIO_ZONE = 0x0A;
	int OBJ_TYPE_EXP = 0x0B;
	int OBJ_TYPE_CONSOLE = 0x0C;
	int OBJ_TYPE_USER_SETTING = 0x0D;
	int OBJ_TYPE_CONTROL_READER = 0x0E;
	int OBJ_TYPE_CONTROL_LOCK = 0x0F;

	int getMessageType();
}
