/*
 * Sample program for use with Product         
 *  ProgIds: 5724-J06 5724-J05 5724-J04 5697-J09 5655-M74 5655-M75 5648-C63
 *  (C) Copyright IBM Corporation 2004.                     
 * All Rights Reserved * Licensed Materials - Property of IBM
 *
 * This sample program is provided AS IS and may be used, executed,
 * copied and modified without royalty payment by customer
 *
 * (a) for its own instruction and study,
 * (b) in order to develop applications designed to run with an IBM
 *     WebSphere product, either for customer's own internal use or for
 *     redistribution by customer, as part of such an application, in
 *     customer's own products.
 */
package cmp.common;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/***************************************************************************** 
 * <P>The ResourcesHandler is used by several Configuration Manager Proxy
 * Sample programs.
 *
 * <P><TABLE BORDER="1" BORDERCOLOR="#000000" CELLSPACING="0"
 * CELLPADDING="5" WIDTH="100%">
 * <TR>
 *   <TD COLSPAN="2" ALIGN="LEFT" VALIGN="TOP" BGCOLOR="#C0FFC0">
 *     <B><I>cmp.ResourcesHandler</I></B><P>
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Responsibilities</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     Provides services to provide localised strings and user settings.
 *   </TD>
 * </TR>
 * <TR>
 *   <TD WIDTH="18%" ALIGN="LEFT" VALIGN="TOP">Internal Collaborators</TD>
 *   <TD WIDTH="*" ALIGN="LEFT" VALIGN="TOP">
 *     None
 *   </TD>
 * </TR>
 * </TABLE>
 *
 * <pre>
 * Change Activity:
 * -------- ----------- -------------   ------------------------------------
 * Reason:  Date:       Originator:     Comments:
 * -------- ----------- -------------   ------------------------------------
 * 25103.7  2004-03-18  HDMPL           v6 Release
 * </pre>
 *
 * @version Samples/ConfigManagerProxy/cmp/common/ResourcesHandler.java, Config.Proxy, S000, S000-L50818.2 1.34
 *****************************************************************************/
public class ResourcesHandler {
    
    /**
     * This is the ResourcesHandler which supplies
     * translatable strings
     */
    private static ResourcesHandler nlsHandler = null;
    
    /**
     * This is the ResourcesHandler which supplies
     * user configurable settings
     */
    private static ResourcesHandler settingsHandler = null;
    
    
    /**
     * The bundle of resources managed by this handler instance
     */
    private ResourceBundle resources;
    
    /**
     * Cache of the resources contained in the bundle
     */
    private Properties resourcesCache = null;
    
    
    /**
     * This constant describes the name of the bundle from which
     * translatable strings should be obtained. The naming rules for
     * Java resource bundles apply.
     */
    private final static String NLS_RESOURCE_FILENAME = "cmp.common.CMPSamples_Resources";
    
    /**
     * This constant describes the name of the bundle from which
     * user settings should be obtained. The naming rules for
     * Java resource bundles apply.
     */
    private final static String SETTINGS_RESOURCE_FILENAME = "CMPSamples_UserSettings";
    
    
    // A set of strings that describe the keys used to look up
    // string constants in the NLS bundle.
    public static final String WINDOW_TITLE = "exerciser.windowtitle"; 
    public static final String WELCOME = "exerciser.welcome";
    public static final String CLEAR_CONSOLE = "exerciser.clearconsole";
    public static final String SELECT_TRACE_OUTPUT = "exerciser.select_trace_output";
    public static final String SELECT_SCRIPT_OUTPUT = "exerciser.select_script_output";
    public static final String SELECT_SCRIPT_INPUT = "exerciser.select_script_input";
    public static final String SCRIPT_WAIT_TIME = "exerciser.script_wait_time";
    public static final String BROKER_LONG_DESC = "exerciser.broker_long_desc";
    public static final String BROKER_SHORT_DESC = "exerciser.broker_short_desc";
    public static final String TOPOLOGY_NAME = "exerciser.topology_name";
    public static final String TOPOLOGY_LONG_DESC = "exerciser.topology_long_desc";
    public static final String TOPOLOGY_SHORT_DESC = "exerciser.topology_short_desc";
    public static final String COLLECTIVE_LONG_DESC = "exerciser.collective_long_desc";
    public static final String COLLECTIVE_SHORT_DESC = "exerciser.collective_short_desc";
    public static final String EG_LONG_DESC = "exerciser.eg_long_desc";
    public static final String EG_SHORT_DESC = "exerciser.eg_short_desc";
    public static final String TOPIC_NAME = "exerciser.topic_name";
    public static final String SSL_KEYRING = "exerciser.ssl_keyring";
    public static final String SSL_PASSWORD = "exerciser.ssl_password";
    public static final String SSL_CONNECTOR_ENABLED = "exerciser.ssl_connector_enabled";
    public static final String TEMP_TOPIC_QOP = "exerciser.temp_topic_qop";
    public static final String SYS_QOP = "exerciser.sys_qop";
    public static final String ISYS_QOP = "exerciser.isys_qop";
    public static final String IB_HOST = "exerciser.interbroker_host";
    public static final String IB_PORT = "exerciser.interbroker_port";
    public static final String AUTH_PROTOCOLS = "exerciser.auth_protocols";
    public static final String MULTICAST_ENABLE = "exerciser.multicast_enable";
    public static final String MULTICAST_IPV4_MINIMUM_ADDRESS = "exerciser.multicast_ipv4_minimum_address";
    public static final String MULTICAST_IPV4_MAXIMUM_ADDRESS = "exerciser.multicast_ipv4_maximum_address";
    public static final String MULTICAST_DATA_PORT = "exerciser.multicast_data_port";
    public static final String MULTICAST_PACKET_SIZE = "exerciser.multicast_packet_size";
    public static final String MULTICAST_HB_TIMEOUT = "exerciser.multicast_hb_timeout";
    public static final String MULTICAST_TTL = "exerciser.multicast_ttl";
    public static final String MULTICAST_IPV4_NETWORK_INTERFACE = "exerciser.multicast_ipv4_network_interface";
    public static final String MULTICAST_ISRELIABLE = "exerciser.multicast_isreliable";
    public static final String MULTICAST_ISSECURE = "exerciser.multicast_issecure";
    public static final String MULTICAST_OVERLAPPING_TOPIC = "exerciser.multicast_overlapping_topic";
    public static final String MULTICAST_LIMIT_TRANS = "exerciser.multicast_limit_trans";
    public static final String MULTICAST_TRL = "exerciser.multicast_trl";
    public static final String MULTICAST_BACKOFF_TIME = "exerciser.multicast_backoff_time";
    public static final String MULTICAST_NACK_CHECK = "exerciser.multicast_nack_check";
    public static final String MULTICAST_PACKET_BUFFERS = "exerciser.multicast_packet_buffers";
    public static final String MULTICAST_PROTOCOL_TYPE = "exerciser.multicast_protocol_type";
    public static final String MULTICAST_MAX_MEMORY_ALLOWED = "exerciser.multicast_max_memory_allowed";
    public static final String MULTICAST_SOCKET_BUFFER = "exerciser.multicast_socket_buffer";
    public static final String MULTICAST_HISTORY_CLEANING_TIME = "exerciser.multicast_history_cleaning_time";
    public static final String MULTICAST_MINIMAL_HISTORY_SIZE = "exerciser.multicast_minimal_history_size";
    public static final String MULTICAST_NACK_ACC_TIME = "exerciser.multicast_back_acc_time";
    public static final String MULTICAST_MAX_KEY_AGE = "exerciser.multicast_max_key_age";
    public static final String TOPIC_QOP = "exerciser.topic_qop";
    public static final String TOPIC_MULTICAST_ENABLED = "exerciser.topic_multicast_enabled";
    public static final String TOPIC_MULTICAST_IPV4_GROUP_ADDRESS = "exerciser.topic_multicast_ipv4_group_address";
    public static final String TOPIC_MULTICAST_ENCRYPTED = "exerciser.topic_multicast_encrypted";
    public static final String TOPIC_MULTICAST_RELIABLE = "exerciser.topic_multicast_reliable";
    public static final String SELECT_BAR = "exerciser.select_bar";
    public static final String IMMEDIATE_STOP = "exerciser.immediate_stop";
    public static final String FILE = "exerciser.file";
    public static final String ACTION_ADDED_TO_BATCH = "exerciser.action_added_to_batch";
    public static final String ACTION_SENT_TO_CM = "exerciser.action_sent_to_cm";
    public static final String PROPERTY_NAME = "exerciser.property_name";
    public static final String PROPERTY_VALUE = "exerciser.property_value";
    public static final String SELECTED = "exerciser.selected";
    public static final String SELECT_CONFIGMGR_FILE = "exerciser.select_configmgr_file";
    public static final String PROGRAM_STARTED = "exerciser.program_started";
    public static final String ADMINISTEREDOBJECT_REFRESH = "exerciser.refresh";
    public static final String ADMINISTEREDOBJECT_GETCHILDREN = "exerciser.get_children";
    public static final String ADMINISTEREDOBJECT_RAWPROPERTIES = "exerciser.raw_properties";
    public static final String CM_GETSUBSCRIPTIONS = "exerciser.get_subscriptions";
    public static final String DELETING_SUBSCRIPTION = "exerciser.deleting_subscription";
    public static final String CM_CANCEL_DEPLOY = "exerciser.cm_cancel_deploy";
    public static final String CM_STARTSYSTRACE = "exerciser.start_sys_trace";
    public static final String CM_DEBUGSYSTRACE = "exerciser.debug_sys_trace";
    public static final String CM_STOPSYSTRACE = "exerciser.stop_sys_trace";
    public static final String CM_CUSTOM = "exerciser.custom";
    public static final String FILE_CONNECT = "exerciser.connect";
    public static final String FILE_DISCONNECT = "exerciser.disconnect";
    public static final String FILE_CMP_TRACE = "exerciser.cmp_trace";
    public static final String FILE_CONNECTUSINGPROPERTIESFILE = "exerciser.connect_using_properties_file";
    public static final String TOPOLOGY_CREATEBROKER = "exerciser.topology_create_broker";
    public static final String TOPOLOGY_CREATECOLLECTIVE = "exerciser.topology_create_collective";
    public static final String TOPOLOGY_CREATECONNECTION = "exerciser.topology_create_connection";
    public static final String BROKER_DELETE = "exerciser.broker_delete";
    public static final String BROKER_CANCEL_DEPLOY = "exerciser.broker_cancel_deploy";
    public static final String COLLECTIVE_DELETE = "exerciser.collective_delete";
    public static final String TOPOLOGY_DELETECONNECTION = "exerciser.topology_delete_connection";
    public static final String TOPOLOGY_DEPLOY = "exerciser.topology_deploy";
    public static final String TOPOLOGY_LISTCONNECTIONS = "exerciser.topology_list_connections";
    public static final String REMOVE_DELETED_BROKER = "exerciser.remove_deleted_broker";
    public static final String DELETED_BROKER_TO_REMOVE_NAME_OR_UUID = "exerciser.deleted_broker_to_remove_name_or_uuid";
    public static final String TOPOLOGY_PROPERTIES = "exerciser.topology_properties";
    public static final String COLLECTIVE_ADDBROKERS = "exerciser.collective_add_brokers";
    public static final String COLLECTIVE_REMOVEBROKERS = "exerciser.collective_remove_brokers";
    public static final String COLLECTIVE_PROPERTIES = "exerciser.collective_properties";
    public static final String BROKER_CREATEEG = "exerciser.broker_create_eg";
    public static final String EG_DELETE = "exerciser.eg_delete";
    public static final String BROKER_DEPLOY = "exerciser.broker_deploy";
    public static final String BROKER_DELETEALLEGSANDDEPLOY = "exerciser.broker_delete_and_deploy";
    public static final String EG_STARTFLOWS = "exerciser.eg_start_flows";
    public static final String EG_STOPFLOWS = "exerciser.eg_stop_flows";
    public static final String EG_STARTUSERTRACE = "exerciser.eg_start_user_trace";
    public static final String EG_DEBUGUSERTRACE = "exerciser.eg_debug_user_trace";
    public static final String EG_STOPUSERTRACE = "exerciser.eg_stop_user_trace";
    public static final String BROKER_LISTCONNECTIONS = "exerciser.broker_list_connections";
    public static final String BROKER_SET_UUID = "exerciser.broker_set_uuid";
    public static final String BROKER_FORCE_SUBSCRIBE = "exerciser.broker_force_subscribe";
    public static final String BROKER_MULTICASTPROPERTIES = "exerciser.broker_multicast_properties";
    public static final String BROKER_PROPERTIES = "exerciser.broker_properties";
    public static final String BROKER_DOES_NOT_EXIST = "exerciser.broker_does_not_exist";
    public static final String EG_DEPLOY = "exerciser.eg_deploy";
    public static final String EG_DELETEDEPLOYED = "exerciser.eg_delete_deployed";
    public static final String EG_PROPERTIES = "exerciser.eg_properties";
    public static final String EG_DOES_NOT_EXIST = "exerciser.eg_does_not_exist";
    public static final String MF_START = "exerciser.mf_start";
    public static final String MF_STOP = "exerciser.mf_stop";
    public static final String MF_DELETE = "exerciser.mf_delete";
    public static final String MF_STARTUSERTRACE = "exerciser.mf_start_user_trace";
    public static final String MF_DEBUGUSERTRACE = "exerciser.mf_debug_user_trace";
    public static final String MF_STOPUSERTRACE = "exerciser.mf_stop_user_trace";
    public static final String LOG_DISPLAY = "exerciser.log_display";
    public static final String LOG_CLEAR = "exerciser.log_clear";
    public static final String TOPIC_CREATE = "exerciser.topic_create";
    public static final String TOPIC_DELETE = "exerciser.topic_delete";
    public static final String TOPIC_MOVE = "exerciser.topic_move";
    public static final String TOPICROOT_DISPLAYUSERS = "exerciser.topicroot_display_users";
    public static final String TOPICROOT_DISPLAYGROUPS = "exerciser.topicroot_display_groups";
    public static final String TOPICROOT_DISPLAYPUBLICGROUPS = "exerciser.topicroot_display_public_groups";
    public static final String TOPICROOT_DEPLOY = "exerciser.topicroot_deploy";
    public static final String TOPICROOT_MODIFYDEFAULTPOLICY = "exerciser.topicroot_modify_default_policy";
    public static final String TOPIC_ADDPOLICY = "exerciser.topic_add_policy";
    public static final String TOPIC_REMOVEPOLICY = "exerciser.topic_remove_policy";
    public static final String TOPIC_PROPERTIES = "exerciser.topic_properties";
    public static final String TOPIC_ADDDEFAULTPOLICY = "exerciser.topic_add_default_policy";
    public static final String TOPIC_REMOVEDEFAULTPOLICY = "exerciser.topic_remove_default_policy";
    public static final String TOPIC_MODIFYDEFAULTPOLICY = "exerciser.topic_modify_default_policy";
    public static final String NO_OPTIONS_AVAILABLE = "exerciser.no_options_available";
    public static final String YES_INPUT_STRING_IDENTIFIER = "exerciser.yes_input_string_identifier";
    public static final String USING_SEC_EXIT = "exerciser.using_sec_exit";
    public static final String CONNECTION_COMPLETED = "exerciser.connection_completed";
    public static final String NOT_CONNECTED = "exerciser.not_connected";
    public static final String AUTOMATION_WARNING = "exerciser.automation_warning";
    public static final String ADDED_AUTOMATION_ACTION = "exerciser.added_automation_action";
    public static final String ACTION_IGNORED = "exerciser.action_ignored";
    public static final String RECORDING_STARTED = "exerciser.recording_started";
    public static final String RECORDING_STOPPED = "exerciser.recording_stopped";
    public static final String PLAYBACK_STARTED = "exerciser.playback_started";
    public static final String PLAYBACK_INFO = "exerciser.playback_info";
    public static final String PLAYBACK_FINISHED = "exerciser.playback_finished";
    public static final String PLAYBACK_FILE_NOT_FOUND = "exerciser.playback_file_not_found";
    public static final String PLAYBACK_FILE_NOT_READABLE = "exerciser.playback_file_not_readable";
    public static final String PLAYBACK_FILE_EMPTY = "exerciser.playback_file_empty";
    public static final String COMMAND_IGNORED = "exerciser.command_ignored";
    public static final String BATCH_START = "exerciser.batch_start_info";
    public static final String BATCH_SENT = "exerciser.batch_sent_info";
    public static final String BATCH_CLEARED = "exerciser.batch_cleared_info";
    public static final String FILE_RETRYCHARS = "exerciser.file_retrychars";
    public static final String FILE_QUIT = "exerciser.file_exit";
    public static final String FILE_BATCHCLEAR = "exerciser.batch_cleared_menu";
    public static final String FILE_BATCHSEND = "exerciser.batch_send_menu";
    public static final String FILE_BATCHSTART = "exerciser.batch_start_menu";
    public static final String FILE_AUTOGETCHILDREN = "exerciser.auto_get_children";
    public static final String FILE_ISDELTA = "exerciser.is_delta";
    public static final String FILE_SHOWADVANCED = "exerciser.show_advanced";
    public static final String FILE_MQJAVATRACE = "exerciser.mq_java_trace";
    public static final String AUTOMATION = "exerciser.automation";
    public static final String AUTOMATION_RECORD = "exerciser.automation_record";
    public static final String AUTOMATION_PAUSE = "exerciser.automation_pause";
    public static final String AUTOMATION_PLAY = "exerciser.automation_play";
    public static final String AUTOMATION_STOP = "exerciser.automation_stop";
    public static final String ADDTOBATCH = "exerciser.add_to_batch";
    public static final String SUBMIT = "exerciser.submit";
    public static final String CANCEL = "exerciser.cancel";
    public static final String NEW_SUBCOMPONENT = "exerciser.new_subcomponent";
    public static final String REMOVED_SUBCOMPONENT = "exerciser.removed_subcomponent";
    public static final String CHANGED_ATTRIBUTE = "exerciser.changed_attribute";
    public static final String LOG_ENTRY = "exerciser.log_entry";
    public static final String REFERENCE_PROPERTY = "exerciser.reference_property";
    public static final String NO_CUSTOM_TEST_DEFINED = "exerciser.no_custom_test_defined";
    public static final String MQ_TRACE_STARTED = "exerciser.mq_trace_started";
    public static final String MQ_TRACE_STOPPED = "exerciser.mq_trace_stopped";
    public static final String NEVER = "exerciser.never";
    public static final String CONSOLE_SAVED = "exerciser.console_saved";
    public static final String NO_CONNECTIONS = "exerciser.no_connections";
    public static final String CMP_TRACE_STARTED = "exerciser.cmp_trace_started";
    public static final String CMP_TRACE_STOPPED = "exerciser.cmp_trace_stopped";
    public static final String NO_DEPLOYED_DEPENDENCIES = "exerciser.no_deployed_dependencies";
    public static final String LOG_EMPTY = "exerciser.log_empty";
    public static final String INVALID_TYPE = "exerciser.invalid_type";
    public static final String INVALID_PATH_TO_TOPIC = "exerciser.invalid_path_to_topic";
    public static final String INVALID_PATH_TO_NEW_TREE = "exerciser.invalid_path_to_new_tree";
    public static final String NO_DEFAULT_POLICY_DEFINED = "exerciser.no_default_policy_defined";
    public static final String NO_POLICIES_DEFINED = "exerciser.no_policies_defined";
    public static final String NO_SUCH_TOPIC = "exerciser.no_such_topic";
    public static final String NO_USERS_DEFINED = "exerciser.no_users_defined";
    public static final String NO_GROUPS_DEFINED = "exerciser.no_groups_defined";
    public static final String NO_PUBLIC_GROUPS_DEFINED = "exerciser.no_public_groups_defined";
    public static final String INVALID_ARGUMENTS = "exerciser.invalid_arguments";
    public static final String INVALID_PERMISSION = "exerciser.invalid_permission";
    public static final String INVALID_USER_TYPE = "exerciser.invalid_user_type";
    public static final String INVALID_USER_NAME = "exerciser.invalid_user_name";
    public static final String NOT_AUTHORISED = "exerciser.not_authorised";
    public static final String GRANT_ACCESS = "exerciser.grant_access";
    public static final String REMOVE_ACCESS = "exerciser.remove_access";
    public static final String GRANT_ACCESS_CM = "exerciser.grant_access_cm";
    public static final String REMOVE_ACCESS_CM = "exerciser.remove_access_cm";
    public static final String GRANT_ACCESS_SUBS = "exerciser.grant_access_subs";
    public static final String REMOVE_ACCESS_SUBS = "exerciser.remove_access_subs";
    public static final String ACCESS_FULL = "exerciser.access_full";
    public static final String ACCESS_EDIT = "exerciser.access_edit";
    public static final String ACCESS_VIEW = "exerciser.access_view";
    public static final String ACCESS_DEPLOY = "exerciser.access_deploy";
    public static final String USER = "exerciser.user";
    public static final String GROUP = "exerciser.group";
    public static final String NOT_APPLICABLE = "exerciser.not_applicable";
    public static final String MARKED_ACENTRY_FOR_REMOVAL = "exerciser.marked_acentry_for_removal";
    public static final String SHOW_ACCESS = "exerciser.show_access";
    public static final String SHOW_SUBS_ACCESS = "exerciser.show_subs_access";
    public static final String SHOW_ACCESS_LINE = "exerciser.show_access_line";
    public static final String TOO_MANY_SUBS_TO_DISPLAY = "exerciser.too_many_subs_to_display";
    public static final String TOO_MANY_SUBS_TO_DELETE = "exerciser.too_many_subs_to_delete";
    public static final String VIEW = "exerciser.view";
    public static final String OBJECT_UNAVAILABLE = "exerciser.object_unavailable";
    public static final String DOMAININFO_HELP = "domaininfo.help";
    public static final String BROKER_RUNNING = "domaininfo.broker_running";
    public static final String EG_RUNNING = "domaininfo.eg_running";
    public static final String MF_RUNNING = "domaininfo.mf_running";
    public static final String BROKER_STOPPED = "domaininfo.broker_stopped";
    public static final String EG_STOPPED = "domaininfo.eg_stopped";
    public static final String MF_STOPPED = "domaininfo.mf_stopped";
    public static final String BROKER_DELETED = "domaininfo.broker_deleted";
    public static final String EG_DELETED = "domaininfo.eg_deleted";
    public static final String MF_DELETED = "domaininfo.mf_deleted";
    public static final String BROKER_ADDED = "domaininfo.broker_added";
    public static final String EG_ADDED = "domaininfo.eg_added";
    public static final String MF_ADDED = "domaininfo.mf_added";
    public static final String LISTENING = "domaininfo.listening";
    public static final String CONNECT_FAILED = "common.connect_failed";
    public static final String CONNECTING = "common.connecting";
    public static final String CONNECT_IN_PROGRESS = "common.connect_in_progress";
    public static final String CONNECTED_TO_QMGR = "common.connected_to_qmgr";
    public static final String CONNECTED_TO_CM = "common.connected_to_cm";
    public static final String CONNECTED_TO_CM_ON = "common.connected_to_cm_on";
    public static final String REGISTERED_LISTENER = "common.registered_listener";
    public static final String NO_RESPONSE_FROM_CM = "common.no_response_from_cm";
    public static final String CONNECTION_CANCELLING = "common.connection_cancelling";
    public static final String CONNECTION_CANCELLED = "common.connection_cancelled";
    public static final String DISCONNECTED = "common.disconnected";
    public static final String COMMAND_THREAD_BUSY = "exerciser.command_thread_busy";
    public static final String PLEASE_WAIT = "exerciser.please_wait";
    public static final String PLEASE_WAIT_VERBOSE = "exerciser.please_wait_verbose";
    public static final String INCOMPATIBLE_SCRIPT_VERSION = "exerciser.incompatible_script";
    public static final String BROKER_DEPLOY_REQUIRED = "exerciser.broker_deploy_required";
    public static final String TOPOLOGY_DEPLOY_REQUIRED = "exerciser.topology_deploy_required";
    public static final String TOPIC_DEPLOY_REQUIRED = "exerciser.topic_deploy_required";
    public static final String NEW_BROKER_ANY_DEPLOY_REQUIRED = "exerciser.new_broker_any_deploy_required";
    
    // A set of strings that describe the keys used to look up
    // string constants in the user settings bundle.
    public static final String AUTO_GET_CHILDREN = "exerciser.auto_get_children";
    public static final String INCREMENTAL_DEPLOY = "exerciser.incremental_deploy";
    public static final String SHOW_ADVANCED_PROPERTIES = "exerciser.show_advanced_properties";
    public static final String ENABLE_MQ_TRACE = "exerciser.enable_mq_trace";
    
        
    // A set of strings that describe keys used in both bundles
    public static final String HOSTNAME = "hostname";
    public static final String PORT = "port";
    public static final String QMGR = "qmgr";
    public static final String SECEXIT_CLASS = "secexit_class";
    public static final String SECEXIT_URL = "secexit_url";
    public static final String BROKER_NEW_UUID = "exerciser.broker_new_uuid";
    public static final String LOGGER_CLASSNAME = "exerciser.logger_classname";
    public static final String LOGGER_PARAMETER = "exerciser.logger_parameter";
    public static final String CONFIG_RETRY_TIME = "exerciser.config_retry_time";
    public static final String SAVE_CONSOLE = "exerciser.saveconsole";
    public static final String TOPIC_SPEC = "exerciser.topic_spec";
    public static final String BROKER_SPEC = "exerciser.broker_spec";
    public static final String USER_SPEC = "exerciser.user_spec";
    public static final String SUBS_POINTS = "exerciser.subs_points";
    public static final String START_DATE = "exerciser.start_date";
    public static final String END_DATE = "exerciser.end_date";
    public static final String DELETE_MATCHING_SUBS = "exerciser.deletesubs";
    public static final String PARAMETER_1 = "exerciser.parameter1";
    public static final String PARAMETER_2 = "exerciser.parameter2";
    public static final String NEW_BROKER_NAME = "exerciser.new_broker_name";
    public static final String NEW_BROKER_QMGR = "exerciser.new_broker_qmgr";
    public static final String CREATE_DEFAULT_EG = "exerciser.create_default_eg";
    public static final String SOURCE_BROKER_NAME = "exerciser.source_broker_name";
    public static final String TARGET_BROKER_NAME = "exerciser.target_broker_name";
    public static final String COLLECTIVE_NAME = "exerciser.collective_name";
    public static final String BROKER_NAME = "exerciser.broker_name";
    public static final String NEW_EG_NAME = "exerciser.new_eg_name";
    public static final String NEW_COLLECTIVE_NAME = "exerciser.new_collective_name";
    public static final String BROKER_LIST_ADD = "exerciser.broker_list_add";
    public static final String BROKER_LIST_REMOVE = "exerciser.broker_list_remove";
    public static final String NEW_TOPIC_NAME = "exerciser.new_topic_name";
    public static final String POLICY_PRINCIPAL_NAME = "exerciser.policy_principal_name";
    public static final String POLICY_TYPE = "exerciser.policy_type";
    public static final String POLICY_PUBLISH = "exerciser.policy_publish";
    public static final String POLICY_SUBSCRIBE = "exerciser.policy_subscribe";
    public static final String POLICY_PERSIST = "exerciser.policy_persist";
    public static final String DEPLOYED_OBJECTS_TO_REMOVE = "exerciser.objects_to_remove";
    public static final String MOVE_TOPIC_NAME = "exerciser.move_topic_name";
    public static final String MOVE_TOPIC_PARENT = "exerciser.move_topic_parent";
    public static final String EG_NAME = "exerciser.eg_name";
    public static final String DEPLOY_WAIT_TIME = "exerciser.deploy_wait_time";
    public static final String TREE_HELP = "exerciser.tree_help";
    public static final String TREE_HELP_2 = "exerciser.tree_help_2";
    public static final String ACL_NAME = "exerciser.acl_name";
    public static final String ACL_DOMAIN_GRANT = "exerciser.acl_domain_grant";
    public static final String ACL_DOMAIN_REMOVE = "exerciser.acl_domain_remove";
    public static final String ACL_TYPE = "exerciser.acl_type";
    public static final String ACL_PERMISSION = "exerciser.acl_permission";
    
    
    /**
     * Creates a resource handler that uses the supplied filename
     * for obtaining resources.
     * @param bundleName filename of the resource to use. The resource
     * file is loaded using Java's resource bundle loader, which means that
     * a locale specific bundle will be attempted to be loaded before
     * a general one.
     */
    private ResourcesHandler(String bundleName) {
        try {
            resources = ResourceBundle.getBundle(bundleName);
            cacheAllResources();
        } catch (MissingResourceException ex) {
            System.err.println("Warning: Can't find resource bundle '"+bundleName+"'");
            resources = null;
        }
    }

    /**
     * Requests a resource from the bundle that contains localisable strings
     * @param keyName constant used to identify the requested resource
     * @param defaultValue Value to return if the resource could not
     * be found
     * @return String containing the value of the requested resource,
     * or an empty string if the resource could not be found.
     */
    public synchronized static String getNLSResource(String keyName) {
        if (nlsHandler == null) {
            nlsHandler = new ResourcesHandler(NLS_RESOURCE_FILENAME);
        }
        return nlsHandler.getResource(keyName, "");
    }
    
    /**
     * Requests a resource from the bundle that contains localisable strings
     * and substitutes any %n characters with the appropriate string
     * from the supplied array.
     * @param keyName constant used to identify the requested resource
     * @param defaultValue Value to return if the resource could not
     * be found
     * @return String containing the value of the requested resource,
     * or an empty string if the resource could not be found.
     */
    public synchronized static String getNLSResource(String keyName, String[] substitutions) {
        return MessageFormat.format(getNLSResource(keyName), substitutions);
    }
    
    /**
     * Requests a resource from the bundle that contains user settings
     * @param keyName constant used to identify the requested resource
     * @param defaultValue Value to return if the resource could not
     * be found
     * @return String containing the value of the requested resource,
     * or null if the resource could not be found.
     */
    public synchronized static String getUserSetting(String keyName) {
        return getUserSetting(keyName, null);
    }
    
    /**
     * Requests a resource from the bundle that contains user settings
     * @param keyName constant used to identify the requested resource
     * @param defaultValue Value to return if the resource could not
     * be found
     * @return String containing the value of the requested resource,
     * or defaultValue if the resource could not be found.
     */
    public synchronized static String getUserSetting(String keyName, String defaultValue) {
        if (settingsHandler == null) {
            settingsHandler = new ResourcesHandler(SETTINGS_RESOURCE_FILENAME);
        }
        return settingsHandler.getResource(keyName, defaultValue);
    }
    
    /**
     * Requests a resource from the bundle that contains user settings
     * and returns the value as an int.
     * @param keyName constant used to identify the requested resource
     * @param defaultValue Value to return if the resource could not
     * be found
     * @return int containing the value of the requested resource,
     * or defaultValue if the resource could not be found.
     */
    public synchronized static int getUserSettingInt(String keyName, int defaultValue) {
        
        int retVal = defaultValue;
        String valueString = getUserSetting(keyName, null);
        if (valueString != null) {
            try {
                retVal = Integer.parseInt(valueString);
            } catch (NumberFormatException ex) {
                // ignore
            }
        }
        return retVal;
    }
    
    /**
     * Requests a resource from the bundle that contains user settings
     * and returns the value as an long.
     * @param keyName constant used to identify the requested resource
     * @param defaultValue Value to return if the resource could not
     * be found
     * @return long containing the value of the requested resource,
     * or defaultValue if the resource could not be found.
     */
    public synchronized static long getUserSettingLong(String keyName, long defaultValue) {
        
        long retVal = defaultValue;
        String valueString = getUserSetting(keyName, null);
        if (valueString != null) {
            try {
                retVal = Long.parseLong(valueString);
            } catch (NumberFormatException ex) {
                // ignore
            }
        }
        return retVal;
    }
    
    /**
     * Requests a resource from the bundle that contains user settings
     * and returns the value as an int.
     * @param keyName constant used to identify the requested resource
     * @param defaultValue Value to return if the resource could not
     * be found
     * @return int containing the value of the requested resource,
     * or defaultValue if the resource could not be found.
     */
    public synchronized static boolean getUserSettingBoolean(String keyName, boolean defaultValue) {        
        boolean retVal = defaultValue;
        String valueString = getUserSetting(keyName, null);
        if (valueString != null) {
            if (valueString.equals("true")) {
                retVal = true;
            } else {
                retVal = false;
            }
        }
        return retVal;
    }
    
    
    
    /**
     * Requests a resource from the current bundle
     * @param keyName Key describing the resource that is sought
     * @param defaultValue Value to return if the resource could not
     * be found
     * @return String containing the value of the requested resource,
     * or defaultValue if the resource could not be found.
     */
    private String getResource(String keyName, String defaultValue) {
        String retVal = null;
        if (resourcesCache != null) {
            retVal = resourcesCache.getProperty(keyName);
        }
        if ((retVal == null) && (resources != null)) {
            try {
                retVal = resources.getString(keyName);
            } catch (MissingResourceException ex) {
                // ignore - the default will be used
            }
        }
        
        if (retVal == null) {
            retVal = defaultValue;
        }
        return retVal;
    }
    
    /**
     * Attempts to modify the resource with the supplied key name
     * to the supplied value. The change is non-persistent until
     * saveUserSettings() is called.
     * @param newKey the key name to change - use a constant
     * from this file rather than a literal
     * @param newValue the new value
     *
     */
    public static void setUserSetting(String newKey, String newValue) {
        if (settingsHandler.resourcesCache == null) {
            settingsHandler.cacheAllResources();
        }
        if (!("".equals(newKey))) {
            settingsHandler.resourcesCache.put(newKey, newValue);
        }
    }
    
    /**
     * Saves the exerciser's user settings to a file
     */
    public static void saveUserSettings() {
        if (settingsHandler.resourcesCache == null) {
            settingsHandler.cacheAllResources();
        }
        
        File f = new File(SETTINGS_RESOURCE_FILENAME + ".properties");
        try {
            settingsHandler.resourcesCache.store(new FileOutputStream(f), " Configuration Settings saved by the Configuration Manager Proxy API Exerciser");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    /**
     * Places all of the resources inside the current bundle
     * and adds them into a hashtable in memory.
     */
    private void cacheAllResources() {
        if (resourcesCache == null) {
            resourcesCache = new Properties();
        }
        if (resources != null) {
	        Enumeration e = resources.getKeys();
	        while (e.hasMoreElements()) {
	            String key = (String)e.nextElement();
	            String value = resources.getString(key);
	            resourcesCache.put(key,value);
	        }
        }
    }

    /**
     * Retrieves the boolean resource with the supplied key name
     * and sets it to true if the current value is false, or to false
     * if the current value is true.
     * The user settings file is then saved to reflect the change.
     * @param keyName Key describing the setting that is required
     * @param newValueIfOldValueUnknown The new value of the setting
     * if the current value is unknown.
     * @return newValue the new value of the setting
     *  
     */
    public static boolean toggleUserSettingBoolean(String keyName, boolean newValueIfOldValueUnknown) {
        boolean newValue = !(getUserSettingBoolean(keyName, !newValueIfOldValueUnknown));
        setUserSetting(keyName, ""+newValue);
        return newValue;
    }


}

