select * from smsnotification n, meeting m, client c, meeting_client cm  where n.meeting_id=m.id and n.client_id=c.id AND
                                                             NOT (m.client_id=c.id OR
                                                            (cm.meeting_id=m.id and cm.clientlist_id =c.id )) ;

