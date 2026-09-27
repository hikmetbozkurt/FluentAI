#!/usr/bin/env python3
"""
Listening Generator for A2 (8 scenarios).
All scenarios adhere to listening.schema.json:
- CEFR: A2
- Valid category enum
- Real speakers
- Timestamped transcript items with text_en and text_tr
- 5 MCQs per scenario using McqBalancer
- Key vocabulary and related_ids with verified IDs
"""

from mcq_balancer import McqBalancer

balancer = McqBalancer(seed=601)

def build_q(qid, q_en, q_tr_hint, correct, distractors, exp_en, exp_tr):
    options, idx = balancer.balance_options(correct, distractors)
    return {
        "id": qid,
        "question_en": q_en,
        "question_tr_hint": q_tr_hint,
        "options": options,
        "correct_answer": correct,
        "explanation_en": exp_en,
        "explanation_tr": exp_tr
    }

A2_LISTENING_SCENARIOS = [
    {
        "id": "listening.a2.it-support-ticket",
        "title": "Helpdesk Call: Password Reset and Account Access",
        "cefr_level": "A2",
        "category": "incident_response",
        "scenario_context": "Can, a new data analyst, calls the company IT helpdesk because his account is locked after entering the wrong password three times.",
        "speakers": [
            {"id": "can", "name": "Can", "role": "Data Analyst", "accent": "Turkish"},
            {"id": "maya", "name": "Maya", "role": "IT Support Specialist", "accent": "British"}
        ],
        "audio_ref": "audio/listening/a2_it_support_ticket.mp3",
        "duration_seconds": 48,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "maya",
                "start_ms": 0,
                "end_ms": 4500,
                "text_en": "Hello, IT Helpdesk, Maya speaking. How can I help you today?",
                "text_tr": "Merhaba, IT Destek Hattı, ben Maya. Bugün size nasıl yardımcı olabilirim?"
            },
            {
                "index": 2,
                "speaker_id": "can",
                "start_ms": 4800,
                "end_ms": 11200,
                "text_en": "Hi Maya, my name is Can. I cannot log into my company email. My account is locked.",
                "text_tr": "Merhaba Maya, benim adım Can. Şirket e-postama giriş yapamıyorum. Hesabım kilitlendi."
            },
            {
                "index": 3,
                "speaker_id": "maya",
                "start_ms": 11500,
                "end_ms": 17800,
                "text_en": "No problem, Can. Did you enter the wrong password multiple times?",
                "text_tr": "Sorun değil Can. Yanlış şifreyi birkaç kez mi girdiniz?"
            },
            {
                "index": 4,
                "speaker_id": "can",
                "start_ms": 18100,
                "end_ms": 25400,
                "text_en": "Yes, I typed it three times incorrectly after updating my laptop system this morning.",
                "text_tr": "Evet, bu sabah dizüstü bilgisayar sistemimi güncelledikten sonra üç kez yanlış yazdım."
            },
            {
                "index": 5,
                "speaker_id": "maya",
                "start_ms": 25700,
                "end_ms": 34200,
                "text_en": "I understand. I am sending a secure temporary reset code to your registered mobile phone now.",
                "text_tr": "Anlıyorum. Şimdi kayıtlı cep telefonunuza güvenli bir geçici sıfırlama kodu gönderiyorum."
            },
            {
                "index": 6,
                "speaker_id": "can",
                "start_ms": 34600,
                "end_ms": 41500,
                "text_en": "I received the SMS code. I will create a new twelve-character password right now.",
                "text_tr": "SMS kodunu aldım. Hemen şimdi on iki karakterli yeni bir şifre oluşturacağım."
            },
            {
                "index": 7,
                "speaker_id": "maya",
                "start_ms": 42000,
                "end_ms": 47500,
                "text_en": "Great! Please call us back if you encounter any other technical difficulties. Have a good day.",
                "text_tr": "Harika! Başka bir teknik sorunla karşılaşırsanız lütfen bizi tekrar arayın. İyi günler."
            }
        ],
        "key_vocabulary": [
            {
                "word": "office",
                "vocab_id": "vocab.office",
                "context_note_tr": "Çalışılan şirket ofisi veya çalışma ortamı."
            },
            {
                "word": "receive",
                "vocab_id": "vocab.receive",
                "context_note_tr": "Mesaj veya sıfırlama kodunu teslim almak."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_a2_it_01",
                "Why is Can unable to access his company email account?",
                "Can neden şirket e-posta hesabına erişememektedir?",
                "His account was locked after he entered an incorrect password three times",
                [
                    "His company laptop was permanently confiscated by building security guards",
                    "He forgot his own full name and employee identification number",
                    "The entire corporate internet network was disconnected for holiday renovations"
                ],
                "Can explains that he entered his password incorrectly three times after a system update, locking the account.",
                "Can, sistem güncellemesinden sonra şifresini üç kez yanlış girdiği için hesabının kilitlendiğini belirtir."
            ),
            build_q(
                "q_a2_it_02",
                "How does Maya deliver the temporary verification code to Can?",
                "Maya geçici doğrulama kodunu Can'a nasıl ulaştırmaktadır?",
                "By sending a secure text message to his registered mobile phone",
                [
                    "By printing a physical letter and mailing it through postal services",
                    "By asking a colleague to announce the code over the office loudspeaker",
                    "By uploading the code publicly to the corporate marketing website"
                ],
                "Maya confirms she is sending a temporary reset code to Can's registered mobile phone via SMS.",
                "Maya, Can'ın kayıtlı cep telefonuna SMS ile geçici bir sıfırlama kodu gönderdiğini teyit eder."
            ),
            build_q(
                "q_a2_it_03",
                "When did Can begin experiencing the password problem?",
                "Can şifre sorununu ne zaman yaşamaya başlamıştır?",
                "This morning after performing a system update on his laptop",
                [
                    "Three weeks ago during an overseas business vacation",
                    "Last year before joining the company's data department",
                    "Late yesterday evening while driving his personal car home"
                ],
                "Can states: 'I typed it three times incorrectly after updating my laptop system this morning.'",
                "Can: 'Bu sabah dizüstü bilgisayar sistemimi güncelledikten sonra üç kez yanlış yazdım' demektedir."
            ),
            build_q(
                "q_a2_it_04",
                "What length of password is Can planning to set up?",
                "Can nasıl bir şifre belirlemeyi planlamaktadır?",
                "A twelve-character password",
                [
                    "A four-digit numerical banking PIN",
                    "A single capital letter with no numbers",
                    "A fifty-word philosophical quotation"
                ],
                "Can confirms: 'I will create a new twelve-character password right now.'",
                "Can: 'Hemen şimdi on iki karakterli yeni bir şifre oluşturacağım' der."
            ),
            build_q(
                "q_a2_it_05",
                "What final advice does Maya offer before ending the phone call?",
                "Maya görüşmeyi sonlandırmadan önce hangi tavsiyede bulunmaktadır?",
                "To call helpdesk back if he encounters further technical difficulties",
                [
                    "To immediately purchase a brand new personal laptop computer",
                    "To refrain from using corporate computers for the rest of the week",
                    "To delete all existing spreadsheet files stored on his hard drive"
                ],
                "Maya politely asks him to call back if he encounters any other technical difficulties.",
                "Maya başka teknik sorunlarla karşılaşması halinde yardım masasını tekrar aramasını söyler."
            )
        ],
        "topic_tags": ["it-support", "helpdesk", "troubleshooting", "workplace-basics"],
        "related_ids": ["vocab.office", "vocab.receive"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.a2.office-equipment-delivery",
        "title": "Delivery Coordination: Ergonomic Office Chairs",
        "cefr_level": "A2",
        "category": "engineering_meeting",
        "scenario_context": "Zeynep and Alex coordinate the arrival of twenty ergonomic chairs and new computer monitors on the third floor.",
        "speakers": [
            {"id": "zeynep", "name": "Zeynep", "role": "HR Assistant", "accent": "Turkish"},
            {"id": "alex", "name": "Alex", "role": "Office Coordinator", "accent": "American"}
        ],
        "audio_ref": "audio/listening/a2_office_equipment.mp3",
        "duration_seconds": 45,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "zeynep",
                "start_ms": 0,
                "end_ms": 5200,
                "text_en": "Alex, the delivery van has just arrived downstairs at the main entrance.",
                "text_tr": "Alex, teslimat kamyoneti az önce ana girişe aşağıya geldi."
            },
            {
                "index": 2,
                "speaker_id": "alex",
                "start_ms": 5500,
                "end_ms": 11500,
                "text_en": "Great! Are all twenty ergonomic office chairs included in this morning's shipment?",
                "text_tr": "Harika! Bu sabahki sevkiyata yirmi ergonomik ofis koltuğunun tümü dahil mi?"
            },
            {
                "index": 3,
                "speaker_id": "zeynep",
                "start_ms": 11800,
                "end_ms": 18200,
                "text_en": "Yes, twenty chairs and ten dual-monitor desk mounts are inside the delivery boxes.",
                "text_tr": "Evet, teslimat kutularının içinde yirmi koltuk ve on adet çift monitörlü masa aparatı var."
            },
            {
                "index": 4,
                "speaker_id": "alex",
                "start_ms": 18500,
                "end_ms": 26000,
                "text_en": "We should use the large service elevator on the left to move them up to the third floor.",
                "text_tr": "Onları üçüncü kata çıkarmak için sol taraftaki büyük servis asansörünü kullanmalıyız."
            },
            {
                "index": 5,
                "speaker_id": "zeynep",
                "start_ms": 26400,
                "end_ms": 33200,
                "text_en": "Good idea. I will ask the facilities team to help us assemble the chairs after lunch.",
                "text_tr": "İyi fikir. Öğle yemeğinden sonra koltukları monte etmemize yardım etmesi için bina teknik ekibine danışacağım."
            },
            {
                "index": 6,
                "speaker_id": "alex",
                "start_ms": 33600,
                "end_ms": 44000,
                "text_en": "Sounds like a solid plan. Let's go sign the delivery receipt right now so the driver can unload.",
                "text_tr": "Sağlam bir plan gibi görünüyor. Sürücünün eşyaları indirebilmesi için hemen şimdi teslimat makbuzunu imzalamaya gidelim."
            }
        ],
        "key_vocabulary": [
            {
                "word": "deliver",
                "vocab_id": "vocab.deliver",
                "context_note_tr": "Sipariş edilen eşyaların adrese teslim edilmesi."
            },
            {
                "word": "office",
                "vocab_id": "vocab.office",
                "context_note_tr": "Şirket çalışma binası ve çalışma alanı."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_a2_deliv_01",
                "How many ergonomic chairs arrived in the delivery van?",
                "Teslimat kamyonetinde kaç adet ergonomik koltuk gelmiştir?",
                "Twenty ergonomic chairs",
                [
                    "Five plastic stools",
                    "One hundred wooden dining chairs",
                    "Three antique armchairs"
                ],
                "Alex asks if all twenty chairs are included, and Zeynep confirms there are twenty chairs.",
                "Alex yirmi koltuğun da dahil olup olmadığını sorar ve Zeynep yirmi koltuk olduğunu onaylar."
            ),
            build_q(
                "q_a2_deliv_02",
                "Which floor will the new equipment be moved to?",
                "Yeni ekipmanlar hangi kata taşınacaktır?",
                "The third floor",
                [
                    "The basement parking lot",
                    "The roof garden terrace",
                    "The tenth floor boardroom"
                ],
                "Alex specifies using the service elevator to take them to the third floor.",
                "Alex eşyaları üçüncü kata çıkarmak için servis asansörünü kullanmayı belirtir."
            ),
            build_q(
                "q_a2_deliv_03",
                "How do Alex and Zeynep plan to transport the heavy delivery boxes upstairs?",
                "Alex ve Zeynep ağır teslimat kutularını yukarıya nasıl taşımayı planlamaktadır?",
                "By using the large service elevator on the left",
                [
                    "By carrying them one by one up the fire escape staircase",
                    "By hiring a commercial helicopter crane",
                    "By sliding them along the hallway floor with ropes"
                ],
                "Alex explicitly suggests: 'We should use the large service elevator on the left.'",
                "Alex: 'Onları çıkarmak için sol taraftaki büyük servis asansörünü kullanmalıyız' der."
            ),
            build_q(
                "q_a2_deliv_04",
                "When will the chairs be assembled?",
                "Koltuklar ne zaman monte edilecektir?",
                "After lunch with assistance from the facilities team",
                [
                    "Next month during the annual company retreat",
                    "Immediately at midnight by external security guards",
                    "During tomorrow's quarterly shareholder meeting"
                ],
                "Zeynep says she will ask facilities for help assembling the chairs after lunch.",
                "Zeynep öğle yemeğinden sonra montaj için teknik ekipten yardım isteyeceğini belirtir."
            ),
            build_q(
                "q_a2_deliv_05",
                "What immediate action must Alex and Zeynep take downstairs?",
                "Alex ve Zeynep'in aşağıda atması gereken acil adım nedir?",
                "Signing the delivery receipt so the driver can unload the boxes",
                [
                    "Paying the driver five thousand dollars in cash banknotes",
                    "Washing the exterior windows of the delivery truck",
                    "Cooking lunch for the warehouse logistics crew"
                ],
                "Alex says: 'Let's go sign the delivery receipt right now so the driver can unload.'",
                "Alex sürücünün indirebilmesi için teslimat makbuzunu imzalamaya gitmeyi önerir."
            )
        ],
        "topic_tags": ["facilities", "office-logistics", "coordination", "equipment"],
        "related_ids": ["vocab.deliver", "vocab.office"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.a2.scheduling-team-lunch",
        "title": "Team Celebration: Organizing Friday Lunch",
        "cefr_level": "A2",
        "category": "stakeholder_alignment",
        "scenario_context": "Burak, a software intern, and Sarah, his engineering manager, discuss arranging a team lunch to celebrate shipping the mobile app update.",
        "speakers": [
            {"id": "burak", "name": "Burak", "role": "Software Intern", "accent": "Turkish"},
            {"id": "sarah", "name": "Sarah", "role": "Engineering Team Lead", "accent": "British"}
        ],
        "audio_ref": "audio/listening/a2_team_lunch.mp3",
        "duration_seconds": 46,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "sarah",
                "start_ms": 0,
                "end_ms": 6100,
                "text_en": "Burak, since our team successfully deployed the app update yesterday, we should organize a team lunch on Friday.",
                "text_tr": "Burak, ekibimiz dün uygulama güncellemesini başarıyla yayına aldığına göre Cuma günü bir ekip yemeği düzenlemeliyiz."
            },
            {
                "index": 2,
                "speaker_id": "burak",
                "start_ms": 6500,
                "end_ms": 12800,
                "text_en": "That sounds wonderful, Sarah! Should we reserve a table at the Italian bistro near the park?",
                "text_tr": "Kulağa harika geliyor Sarah! Parkın yakınındaki İtalyan bistrosunda masa ayırtalım mı?"
            },
            {
                "index": 3,
                "speaker_id": "sarah",
                "start_ms": 13200,
                "end_ms": 20400,
                "text_en": "That restaurant is lovely. However, please remember that Elena is vegetarian and Murat cannot eat gluten.",
                "text_tr": "O restoran çok güzel. Ancak lütfen Elena'nın vejetaryen olduğunu ve Murat'ın glüten tüketemediğini unutma."
            },
            {
                "index": 4,
                "speaker_id": "burak",
                "start_ms": 20800,
                "end_ms": 28500,
                "text_en": "I checked their menu online. They offer certified gluten-free pasta and several fresh vegetarian salads.",
                "text_tr": "İnternetten menülerini kontrol ettim. Sertifikalı glütensiz makarna ve birkaç taze vejetaryen salata sunuyorlar."
            },
            {
                "index": 5,
                "speaker_id": "sarah",
                "start_ms": 29000,
                "end_ms": 36800,
                "text_en": "Excellent research! What time works best between our scheduled code reviews and customer demos?",
                "text_tr": "Mükemmel araştırma! Planlanan kod incelemelerimiz ve müşteri demolarımız arasında en uygun saat hangisi?"
            },
            {
                "index": 6,
                "speaker_id": "burak",
                "start_ms": 37200,
                "end_ms": 45500,
                "text_en": "Everyone is completely free between twelve-thirty and two o'clock. I will book a table for eight people at twelve-thirty.",
                "text_tr": "Herkes on iki buçuk ile iki arası tamamen müsait. On iki buçuğa sekiz kişilik bir masa ayırtacağım."
            }
        ],
        "key_vocabulary": [
            {
                "word": "collaborate",
                "vocab_id": "vocab.collaborate",
                "context_note_tr": "Ekip arkadaşlarıyla ortak etkinlik ve planlamada iş birliği yapmak."
            },
            {
                "word": "customer",
                "vocab_id": "vocab.customer",
                "context_note_tr": "Şirketin ürün ve hizmetlerini kullanan müşteri kitlesi."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_a2_lunch_01",
                "What milestone is the engineering team celebrating with Friday's lunch?",
                "Mühendislik ekibi Cuma günkü yemekle hangi başarıyı kutlamaktadır?",
                "Successfully deploying the mobile app update",
                [
                    "Signing a ten-year corporate office lease agreement",
                    "Moving the entire engineering team to a foreign branch",
                    "Purchasing eighty brand new desktop computers"
                ],
                "Sarah opens by saying: 'since our team successfully deployed the app update yesterday, we should organize a team lunch.'",
                "Sarah ekibin dün uygulama güncellemesini başarıyla yayına almasını kutlamak istediklerini söyler."
            ),
            build_q(
                "q_a2_lunch_02",
                "What dietary restriction does Murat have?",
                "Murat'ın hangi beslenme kısıtlaması bulunmaktadır?",
                "He cannot eat gluten",
                [
                    "He is allergic to drinking clean water",
                    "He strictly eats only raw meat",
                    "He cannot consume any hot cooked food"
                ],
                "Sarah explicitly notes: 'Elena is vegetarian and Murat cannot eat gluten.'",
                "Sarah açıkça Elena'nın vejetaryen olduğunu ve Murat'ın glüten tüketemediğini belirtir."
            ),
            build_q(
                "q_a2_lunch_03",
                "Why is the suggested Italian bistro suitable for the whole team?",
                "Önerilen İtalyan bistrosu tüm ekip için neden uygundur?",
                "It serves certified gluten-free pasta and vegetarian salads",
                [
                    "It gives free laptop chargers to every customer",
                    "It has no chairs so everyone stands during the meal",
                    "It serves only sugary desserts and chocolate shakes"
                ],
                "Burak checked the online menu and confirmed they offer gluten-free pasta and vegetarian options.",
                "Burak internetten menüyü incelemiş ve glütensiz makarna ile vejetaryen seçeneklerin bulunduğunu teyit etmiştir."
            ),
            build_q(
                "q_a2_lunch_04",
                "What time is the lunch reservation scheduled for?",
                "Yemek rezervasyonu saat kaça planlanmaktadır?",
                "Twelve-thirty in the afternoon",
                [
                    "Nine o'clock in the morning",
                    "Six o'clock in the evening",
                    "Eleven o'clock at night"
                ],
                "Burak states: 'I will book a table for eight people at twelve-thirty.'",
                "Burak on iki buçuğa sekiz kişilik masa ayırtacağını belirtir."
            ),
            build_q(
                "q_a2_lunch_05",
                "How many seats will Burak reserve at the restaurant?",
                "Burak restoranda kaç kişilik yer ayırtacaktır?",
                "Eight people",
                [
                    "Two people",
                    "Twenty-five people",
                    "Fifty people"
                ],
                "Burak confirms he is booking a table for eight people.",
                "Burak sekiz kişilik bir masa ayırtacağını ifade eder."
            )
        ],
        "topic_tags": ["team-building", "social", "workplace-culture", "scheduling"],
        "related_ids": ["vocab.collaborate", "vocab.customer"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.a2.new-hire-orientation-call",
        "title": "Onboarding Check-In: First Week Essentials",
        "cefr_level": "A2",
        "category": "product_discovery",
        "scenario_context": "David, a senior mentor, welcomes Deniz, a junior frontend developer, and reviews his first-week orientation schedule and tools.",
        "speakers": [
            {"id": "david", "name": "David", "role": "Senior Engineer", "accent": "American"},
            {"id": "deniz", "name": "Deniz", "role": "Junior Developer", "accent": "Turkish"}
        ],
        "audio_ref": "audio/listening/a2_new_hire_orientation.mp3",
        "duration_seconds": 47,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "david",
                "start_ms": 0,
                "end_ms": 5500,
                "text_en": "Good morning Deniz, and welcome to the team! How was your very first day yesterday?",
                "text_tr": "Günaydın Deniz, ekibe hoş geldin! Dünkü ilk günün nasıl geçti?"
            },
            {
                "index": 2,
                "speaker_id": "deniz",
                "start_ms": 5800,
                "end_ms": 12500,
                "text_en": "Good morning David! It was great. I set up my development environment and configured Git.",
                "text_tr": "Günaydın David! Harikaydı. Geliştirme ortamımı kurdum ve Git ayarlarımı yaptım."
            },
            {
                "index": 3,
                "speaker_id": "david",
                "start_ms": 12900,
                "end_ms": 20000,
                "text_en": "Fantastic. Today our main goal is to review our team channels on Slack and our sprint board on Jira.",
                "text_tr": "Harika. Bugün temel hedefimiz Slack üzerindeki ekip kanallarımızı ve Jira üzerindeki sprint panomuzu incelemek."
            },
            {
                "index": 4,
                "speaker_id": "deniz",
                "start_ms": 20400,
                "end_ms": 28200,
                "text_en": "Sounds good. Where should I post questions if I encounter an error while running the build scripts?",
                "text_tr": "Kulağa iyi geliyor. Derleme komutlarını çalıştırırken bir hatayla karşılaşırsam sorularımı nereye yazmalıyım?"
            },
            {
                "index": 5,
                "speaker_id": "david",
                "start_ms": 28600,
                "end_ms": 37200,
                "text_en": "Always post them in the dev-help channel. We also pair program every afternoon from two to three.",
                "text_tr": "Her zaman dev-help kanalına yaz. Ayrıca her öğleden sonra iki ile üç arası eşli programlama (pair programming) yapıyoruz."
            },
            {
                "index": 6,
                "speaker_id": "deniz",
                "start_ms": 37600,
                "end_ms": 46500,
                "text_en": "That is very reassuring. I will review the documentation and join the pairing session at two o'clock.",
                "text_tr": "Bu çok rahatlatıcı. Dokümantasyonu inceleyip saat ikideki eşli çalışma oturumuna katılacağım."
            }
        ],
        "key_vocabulary": [
            {
                "word": "professional",
                "vocab_id": "vocab.professional",
                "context_note_tr": "İş ortamında sergilenen yetkin ve saygılı profesyonel tutum."
            },
            {
                "word": "support",
                "vocab_id": "vocab.support-v",
                "context_note_tr": "Yeni katılan ekip üyesine rehberlik ve destek sağlama."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_a2_orient_01",
                "What did Deniz successfully finish during his first day at work?",
                "Deniz işteki ilk gününde neyi başarıyla tamamlamıştır?",
                "Setting up his development environment and configuring Git",
                [
                    "Deploying a major architectural database migration to live servers",
                    "Conducting annual performance appraisals for twenty senior directors",
                    "Negotiating an international cloud contract with foreign investors"
                ],
                "Deniz confirms: 'I set up my development environment and configured Git.'",
                "Deniz geliştirme ortamını kurduğunu ve Git yapılandırmasını yaptığını söyler."
            ),
            build_q(
                "q_a2_orient_02",
                "Which Slack channel should Deniz use when asking technical build questions?",
                "Deniz teknik derleme soruları sorarken hangi Slack kanalını kullanmalıdır?",
                "The dev-help channel",
                [
                    "The general-announcements channel",
                    "The executive-boardroom channel",
                    "The random-jokes channel"
                ],
                "David instructs: 'Always post them in the dev-help channel.'",
                "David her zaman dev-help kanalına yazmasını belirtir."
            ),
            build_q(
                "q_a2_orient_03",
                "At what time does the daily pair programming session take place?",
                "Günlük eşli programlama (pair programming) oturumu saat kaçta gerçekleşmektedir?",
                "Between two and three in the afternoon",
                [
                    "At seven o'clock in the early morning",
                    "Between nine and ten at night",
                    "During the Sunday morning weekend breakfast"
                ],
                "David explains: 'We also pair program every afternoon from two to three.'",
                "David her öğleden sonra iki ile üç arası eşli programlama yaptıklarını açıklar."
            ),
            build_q(
                "q_a2_orient_04",
                "What are the two tools David wants Deniz to review today?",
                "David bugün Deniz'in incelemesini istediği iki araç hangisidir?",
                "Slack channels and the Jira sprint board",
                [
                    "Word processing documents and personal bank statements",
                    "Analog telephone lines and printed paper encyclopedias",
                    "Cryptocurrency wallets and video game consoles"
                ],
                "David states the goal is to review team channels on Slack and the sprint board on Jira.",
                "David Slack ekip kanallarını ve Jira sprint panosunu incelemenin bugünkü hedef olduğunu söyler."
            ),
            build_q(
                "q_a2_orient_05",
                "How does Deniz react to learning about the daily pairing session?",
                "Deniz günlük eşli çalışma oturumunu öğrenince nasıl bir tepki vermektedir?",
                "He finds it reassuring and commits to joining at two o'clock",
                [
                    "He becomes angry and threatens to submit his immediate resignation",
                    "He refuses to turn on his computer or speak to his colleagues",
                    "He demands that all pair programming be permanently cancelled"
                ],
                "Deniz expresses relief: 'That is very reassuring. I will review the documentation and join the pairing session at two o'clock.'",
                "Deniz rahatladığını belirterek saat ikideki oturuma katılacağını söyler."
            )
        ],
        "topic_tags": ["onboarding", "mentorship", "developer-tools", "collaboration"],
        "related_ids": ["vocab.professional", "vocab.support-v"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.a2.project-deadline-checkin",
        "title": "Sprint Sync: Documentation Update Status",
        "cefr_level": "A2",
        "category": "engineering_meeting",
        "scenario_context": "Tom, a project manager, checks in with Melis, a frontend engineer, regarding the upcoming deadline for release notes documentation.",
        "speakers": [
            {"id": "tom", "name": "Tom", "role": "Project Manager", "accent": "American"},
            {"id": "melis", "name": "Melis", "role": "Frontend Developer", "accent": "Turkish"}
        ],
        "audio_ref": "audio/listening/a2_project_deadline.mp3",
        "duration_seconds": 44,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "tom",
                "start_ms": 0,
                "end_ms": 5000,
                "text_en": "Hi Melis. Do you have a quick minute to review the documentation release schedule?",
                "text_tr": "Selam Melis. Dokümantasyon yayın takvimini gözden geçirmek için kısa bir vaktin var mı?"
            },
            {
                "index": 2,
                "speaker_id": "melis",
                "start_ms": 5300,
                "end_ms": 11500,
                "text_en": "Sure Tom! I have finished writing the user guide for the new dark mode theme.",
                "text_tr": "Elbette Tom! Yeni karanlık mod teması için kullanıcı kılavuzunu yazmayı bitirdim."
            },
            {
                "index": 3,
                "speaker_id": "tom",
                "start_ms": 11900,
                "end_ms": 18500,
                "text_en": "That is great progress. Are the screenshot tutorials and Turkish translations also completed?",
                "text_tr": "Bu harika bir ilerleme. Ekran görüntüsü anlatımları ve Türkçe çeviriler de tamamlandı mı?"
            },
            {
                "index": 4,
                "speaker_id": "melis",
                "start_ms": 18900,
                "end_ms": 26800,
                "text_en": "The screenshots are uploaded. I am finalizing the Turkish translations and will submit the pull request by four p.m.",
                "text_tr": "Ekran görüntüleri yüklendi. Türkçe çevirileri tamamlıyorum ve saat dörde kadar çekme isteğini (PR) göndereceğim."
            },
            {
                "index": 5,
                "speaker_id": "tom",
                "start_ms": 27200,
                "end_ms": 34800,
                "text_en": "Perfect. If you submit it by four, our technical editor can review and approve it before tomorrow morning.",
                "text_tr": "Kusursuz. Dörde kadar gönderirsen teknik editörümüz yarın sabahtan önce inceleyip onaylayabilir."
            },
            {
                "index": 6,
                "speaker_id": "melis",
                "start_ms": 35200,
                "end_ms": 43500,
                "text_en": "Understood. I will send you a quick direct message as soon as the PR is ready for review.",
                "text_tr": "Anlaşıldı. PR incelemeye hazır olur olmaz sana kısa bir doğrudan mesaj atacağım."
            }
        ],
        "key_vocabulary": [
            {
                "word": "deliver",
                "vocab_id": "vocab.deliver",
                "context_note_tr": "Belirlenen saatte iş çıktısını teslim etmek."
            },
            {
                "word": "tone",
                "vocab_id": "vocab.tone",
                "context_note_tr": "Yazılan dokümantasyondaki dil ve üslup tonu."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_a2_deadl_01",
                "What part of the documentation has Melis already finished?",
                "Melis dokümantasyonun hangi bölümünü çoktan bitirmiştir?",
                "The user guide for the new dark mode theme",
                [
                    "The annual financial audit report for international investors",
                    "The legal terms of service for corporate banking clients",
                    "The physical blueprints for constructing a new office tower"
                ],
                "Melis explains: 'I have finished writing the user guide for the new dark mode theme.'",
                "Melis yeni karanlık mod teması için kullanıcı kılavuzunu yazmayı bitirdiğini söyler."
            ),
            build_q(
                "q_a2_deadl_02",
                "By what time does Melis promise to submit her pull request?",
                "Melis çekme isteğini (PR) saat kaça kadar göndereceğine söz vermektedir?",
                "By four p.m. today",
                [
                    "By midnight on Sunday",
                    "By eight a.m. next Monday",
                    "In three weeks time"
                ],
                "Melis explicitly states she will submit the pull request by four p.m.",
                "Melis saat dörde kadar çekme isteğini göndereceğini açıkça ifade eder."
            ),
            build_q(
                "q_a2_deadl_03",
                "Why is the four p.m. deadline important for Tom?",
                "Saat dört teslim tarihi Tom için neden önemlidir?",
                "It allows the technical editor to review and approve the document before tomorrow morning",
                [
                    "The company internet servers will be permanently shut down at five p.m.",
                    "Tom has a flight to take to an international gaming championship",
                    "All employee badges expire automatically at four-fifteen p.m."
                ],
                "Tom explains that submitting by four allows the technical editor to approve it before tomorrow morning.",
                "Tom dörde kadar gönderilmesinin editörün yarın sabahtan önce inceleyip onaylamasına olanak tanıyacağını söyler."
            ),
            build_q(
                "q_a2_deadl_04",
                "What remaining task is Melis currently working on?",
                "Melis şu anda hangi kalan görev üzerinde çalışmaktadır?",
                "Finalizing the Turkish translations",
                [
                    "Designing new cartoon company logos",
                    "Reinstalling the operating system on Tom's computer",
                    "Ordering forty cups of iced coffee for the team"
                ],
                "Melis says: 'I am finalizing the Turkish translations and will submit the pull request by four p.m.'",
                "Melis Türkçe çevirileri tamamladığını belirtir."
            ),
            build_q(
                "q_a2_deadl_05",
                "How will Melis notify Tom when the pull request is open?",
                "Melis çekme isteği açıldığında Tom'a nasıl haber verecektir?",
                "By sending him a direct message",
                [
                    "By ringing a physical bell in the middle of the hallway",
                    "By printing a notice on the office bulletin board",
                    "By mailing a postcard to his home address"
                ],
                "Melis confirms: 'I will send you a quick direct message as soon as the PR is ready.'",
                "Melis PR hazır olur olmaz kısa bir doğrudan mesaj göndereceğini belirtir."
            )
        ],
        "topic_tags": ["project-management", "deadlines", "documentation", "workflow"],
        "related_ids": ["vocab.deliver", "vocab.tone"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.a2.printer-troubleshooting",
        "title": "Facility Help: Fixing the Shared Office Printer",
        "cefr_level": "A2",
        "category": "incident_response",
        "scenario_context": "Emre, an accountant, encounters an error on the floor printer and asks Lisa, the facilities coordinator, for assistance.",
        "speakers": [
            {"id": "emre", "name": "Emre", "role": "Accountant", "accent": "Turkish"},
            {"id": "lisa", "name": "Lisa", "role": "Facilities Coordinator", "accent": "British"}
        ],
        "audio_ref": "audio/listening/a2_printer_trouble.mp3",
        "duration_seconds": 45,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "emre",
                "start_ms": 0,
                "end_ms": 5500,
                "text_en": "Excuse me, Lisa. The shared printer in corridor B is showing a flashing red warning light.",
                "text_tr": "Afedersin Lisa. B koridorundaki ortak yazıcı yanıp sönen kırmızı bir uyarı ışığı gösteriyor."
            },
            {
                "index": 2,
                "speaker_id": "lisa",
                "start_ms": 5800,
                "end_ms": 11800,
                "text_en": "Hi Emre. Let's take a look. Does the digital display show an error code?",
                "text_tr": "Selam Emre. Bir bakalım. Dijital ekranda bir hata kodu görünüyor mu?"
            },
            {
                "index": 3,
                "speaker_id": "emre",
                "start_ms": 12200,
                "end_ms": 19200,
                "text_en": "Yes, it says 'Paper Jam in Tray Two'. I need to print twenty invoice copies before my meeting.",
                "text_tr": "Evet, '2 Nolu Tepside Kağıt Sıkışması' yazıyor. Toplantımdan önce yirmi fatura kopyası basmam gerekiyor."
            },
            {
                "index": 4,
                "speaker_id": "lisa",
                "start_ms": 19600,
                "end_ms": 28400,
                "text_en": "Don't worry. I will open the side access panel and gently pull out the stuck sheet of paper.",
                "text_tr": "Endişelenme. Yan erişim kapağını açıp sıkışan kağıdı nazikçe dışarı çekeceğim."
            },
            {
                "index": 5,
                "speaker_id": "emre",
                "start_ms": 28800,
                "end_ms": 35500,
                "text_en": "Thank you! The red light just turned green. Should I also refill the blank paper tray?",
                "text_tr": "Teşekkürler! Kırmızı ışık az önce yeşile döndü. Boş kağıt tepsisini de doldurayım mı?"
            },
            {
                "index": 6,
                "speaker_id": "lisa",
                "start_ms": 36000,
                "end_ms": 44500,
                "text_en": "Yes please. A fresh ream of standard A4 paper is stored inside the cabinet beneath the printer.",
                "text_tr": "Evet lütfen. Yazıcının altındaki dolapta yeni bir paket standart A4 kağıdı bulunuyor."
            }
        ],
        "key_vocabulary": [
            {
                "word": "support",
                "vocab_id": "vocab.support-v",
                "context_note_tr": "Ofis içi teknik aksaklıklarda sağlanan operasyonel destek."
            },
            {
                "word": "office",
                "vocab_id": "vocab.office",
                "context_note_tr": "Ortak ofis ekipmanlarının kullanımı ve bakımı."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_a2_print_01",
                "Where is the malfunctioning printer located in the building?",
                "Arızalanan yazıcı binanın neresinde bulunmaktadır?",
                "In corridor B",
                [
                    "Inside the executive underground garage",
                    "On the outdoor cafeteria balcony",
                    "Inside the server room cooling duct"
                ],
                "Emre states: 'The shared printer in corridor B is showing a flashing red warning light.'",
                "Emre B koridorundaki yazıcının kırmızı uyarı ışığı yaktığını söyler."
            ),
            build_q(
                "q_a2_print_02",
                "What exact error message does the printer display?",
                "Yazıcı tam olarak hangi hata mesajını göstermektedir?",
                "Paper Jam in Tray Two",
                [
                    "Total System Memory Overheat",
                    "Invalid Credit Card Payment",
                    "Missing High-Voltage Power Cable"
                ],
                "Emre reads: 'it says Paper Jam in Tray Two.'",
                "Emre '2 Nolu Tepside Kağıt Sıkışması' yazdığını söyler."
            ),
            build_q(
                "q_a2_print_03",
                "Why does Emre urgently need the printer to work?",
                "Emre'nin acilen yazıcıya ihtiyaç duymasının sebebi nedir?",
                "He must print twenty invoice copies before his upcoming meeting",
                [
                    "He wants to print high-resolution vacation photographs",
                    "He is printing five hundred political campaign posters",
                    "He needs to shred old personal identity documents"
                ],
                "Emre explains: 'I need to print twenty invoice copies before my meeting.'",
                "Emre toplantısından önce yirmi fatura kopyası basması gerektiğini belirtir."
            ),
            build_q(
                "q_a2_print_04",
                "How does Lisa resolve the paper jam?",
                "Lisa kağıt sıkışmasını nasıl çözmektedir?",
                "By opening the side panel and gently removing the trapped sheet of paper",
                [
                    "By striking the machine with a heavy steel hammer",
                    "By pouring clean water into the paper intake feed",
                    "By replacing the printer with an entirely new machine"
                ],
                "Lisa says: 'I will open the side access panel and gently pull out the stuck sheet of paper.'",
                "Lisa yan kapağı açıp sıkışan kağıdı nazikçe çıkaracağını açıklar."
            ),
            build_q(
                "q_a2_print_05",
                "Where is the spare A4 paper stored?",
                "Yedek A4 kağıtları nerede saklanmaktadır?",
                "Inside the cabinet located directly beneath the printer",
                [
                    "In a locked warehouse on the other side of town",
                    "Inside Lisa's personal vehicle trunk",
                    "On the top shelf of the corporate kitchen refrigerator"
                ],
                "Lisa specifies: 'stored inside the cabinet beneath the printer.'",
                "Lisa kağıdın yazıcının altındaki dolapta durduğunu söyler."
            )
        ],
        "topic_tags": ["facilities", "troubleshooting", "office-equipment", "workplace-communication"],
        "related_ids": ["vocab.support-v", "vocab.office"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.a2.ordering-catering-supplies",
        "title": "Vendor Call: Restocking Kitchen Snacks and Coffee",
        "cefr_level": "A2",
        "category": "negotiation",
        "scenario_context": "Aylin, an office operations specialist, calls Kevin from a catering supplier to adjust their weekly coffee bean and fruit order.",
        "speakers": [
            {"id": "aylin", "name": "Aylin", "role": "Office Operations Specialist", "accent": "Turkish"},
            {"id": "kevin", "name": "Kevin", "role": "Supplier Sales Representative", "accent": "American"}
        ],
        "audio_ref": "audio/listening/a2_catering_order.mp3",
        "duration_seconds": 47,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "kevin",
                "start_ms": 0,
                "end_ms": 5000,
                "text_en": "Sunrise Catering Supplies, Kevin speaking. How can I assist you today?",
                "text_tr": "Sunrise İkram Malzemeleri, ben Kevin. Bugün size nasıl yardımcı olabilirim?"
            },
            {
                "index": 2,
                "speaker_id": "aylin",
                "start_ms": 5300,
                "end_ms": 13000,
                "text_en": "Hello Kevin, this is Aylin from Nexus Software. I would like to modify our weekly office delivery for Thursday.",
                "text_tr": "Merhaba Kevin, ben Nexus Yazılım'dan Aylin. Perşembe günkü haftalık ofis teslimatımızı güncellemek istiyorum."
            },
            {
                "index": 3,
                "speaker_id": "kevin",
                "start_ms": 13400,
                "end_ms": 19800,
                "text_en": "Of course, Aylin. Your regular order is ten kilograms of dark roast coffee and four boxes of apples.",
                "text_tr": "Elbette Aylin. Normal siparişiniz on kilogram koyu kavrulmuş kahve ve dört kutu elmaydı."
            },
            {
                "index": 4,
                "speaker_id": "aylin",
                "start_ms": 20200,
                "end_ms": 28800,
                "text_en": "We are hosting an engineering workshop this Thursday, so please increase the coffee to fifteen kilograms and add two boxes of bananas.",
                "text_tr": "Bu Perşembe bir mühendislik çalıştayı düzenliyoruz, bu nedenle lütfen kahveyi on beş kilograma çıkarın ve iki kutu muz ekleyin."
            },
            {
                "index": 5,
                "speaker_id": "kevin",
                "start_ms": 29200,
                "end_ms": 38000,
                "text_en": "Got it! Fifteen kilograms of dark roast, four boxes of apples, and two boxes of bananas. Can we deliver by nine a.m.?",
                "text_tr": "Anlaşıldı! On beş kilogram koyu kavrulmuş kahve, dört kutu elma ve iki kutu muz. Sabah saat dokuza kadar teslim edebilir miyiz?"
            },
            {
                "index": 6,
                "speaker_id": "aylin",
                "start_ms": 38400,
                "end_ms": 46500,
                "text_en": "Nine a.m. is perfect before the workshop begins. Please send the revised invoice to our finance team. Thank you!",
                "text_tr": "Sabah dokuz çalıştay başlamadan önce kusursuz. Lütfen güncellenmiş faturayı finans ekibimize gönderin. Teşekkürler!"
            }
        ],
        "key_vocabulary": [
            {
                "word": "customer",
                "vocab_id": "vocab.customer",
                "context_note_tr": "Tedarikçi ile müşteri arasındaki sipariş yönetimi."
            },
            {
                "word": "receive",
                "vocab_id": "vocab.receive",
                "context_note_tr": "Sevkiyat ve güncellenmiş faturanın teslim alınması."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_a2_cater_01",
                "Why does Aylin need to increase the office snack and coffee order?",
                "Aylin ofis atıştırmalık ve kahve siparişini neden artırmak istemektedir?",
                "The company is hosting an engineering workshop on Thursday",
                [
                    "The company cafeteria will be permanently closed by local health inspectors",
                    "All employees have decided to sleep in the office for an entire week",
                    "Aylin wants to open a private grocery store inside her personal home"
                ],
                "Aylin explains: 'We are hosting an engineering workshop this Thursday, so please increase the coffee.'",
                "Aylin bu Perşembe bir mühendislik çalıştayı düzenleyecekleri için siparişi artırdığını belirtir."
            ),
            build_q(
                "q_a2_cater_02",
                "To what quantity does Aylin increase the coffee bean order?",
                "Aylin kahve çekirdeği siparişini hangi miktara yükseltmektedir?",
                "Fifteen kilograms",
                [
                    "Two kilograms",
                    "One hundred kilograms",
                    "Fifty kilograms"
                ],
                "Aylin asks to increase the coffee from ten to fifteen kilograms.",
                "Aylin kahveyi on beş kilograma çıkarmasını ister."
            ),
            build_q(
                "q_a2_cater_03",
                "What new fruit item is added to the delivery?",
                "Teslimata hangi yeni meyve eklenmiştir?",
                "Two boxes of fresh bananas",
                [
                    "Ten watermelons",
                    "Twenty boxes of pineapples",
                    "Five crates of fresh oranges"
                ],
                "Aylin asks to add two boxes of bananas to the existing order of four boxes of apples.",
                "Aylin dört kutu elmaya ek olarak iki kutu muz eklenmesini ister."
            ),
            build_q(
                "q_a2_cater_04",
                "What delivery arrival time is agreed upon?",
                "Hangi teslimat saati üzerinde anlaşmaya varılmıştır?",
                "Nine o'clock on Thursday morning",
                [
                    "Two o'clock in the afternoon",
                    "Six o'clock in the evening",
                    "Midnight on Wednesday"
                ],
                "Kevin asks if nine a.m. works, and Aylin confirms nine a.m. is perfect.",
                "Kevin sabah dokuzun uygun olup olmadığını sorar ve Aylin dokuzun kusursuz olduğunu onaylar."
            ),
            build_q(
                "q_a2_cater_05",
                "Where should Kevin email the revised invoice?",
                "Kevin güncellenmiş faturayı nereye e-postalamalıdır?",
                "To the company finance department",
                [
                    "To Aylin's personal social media account",
                    "To the local municipal tax building",
                    "To an anonymous external internet forum"
                ],
                "Aylin instructs: 'Please send the revised invoice to our finance team.'",
                "Aylin güncellenmiş faturanın finans ekibine gönderilmesini söyler."
            )
        ],
        "topic_tags": ["vendor-management", "operations", "catering", "negotiation"],
        "related_ids": ["vocab.customer", "vocab.receive"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.a2.visitor-badge-reception",
        "title": "Front Desk Reception: Visitor Security Badge",
        "cefr_level": "A2",
        "category": "stakeholder_alignment",
        "scenario_context": "Mark, an external security consultant, arrives at company headquarters and checks in with Ece at the reception desk.",
        "speakers": [
            {"id": "mark", "name": "Mark", "role": "Security Consultant", "accent": "American"},
            {"id": "ece", "name": "Ece", "role": "Receptionist", "accent": "Turkish"}
        ],
        "audio_ref": "audio/listening/a2_visitor_badge.mp3",
        "duration_seconds": 46,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "mark",
                "start_ms": 0,
                "end_ms": 5200,
                "text_en": "Good morning. My name is Mark Vance from SecureNet Consulting. I have a meeting with Karen at ten.",
                "text_tr": "Günaydın. Benim adım SecureNet Danışmanlık'tan Mark Vance. Saat onda Karen ile bir toplantım var."
            },
            {
                "index": 2,
                "speaker_id": "ece",
                "start_ms": 5500,
                "end_ms": 11800,
                "text_en": "Good morning Mr. Vance. Welcome to our headquarters. May I please see a photo ID for registration?",
                "text_tr": "Günaydın Bay Vance. Genel merkezimize hoş geldiniz. Kayıt için fotoğraflı bir kimlik görebilir miyim lütfen?"
            },
            {
                "index": 3,
                "speaker_id": "mark",
                "start_ms": 12100,
                "end_ms": 17200,
                "text_en": "Here is my driver's license. Karen mentioned we would meet in conference room four.",
                "text_tr": "İşte sürücü belgem. Karen dört numaralı konferans salonunda buluşacağımızı söylemişti."
            },
            {
                "index": 4,
                "speaker_id": "ece",
                "start_ms": 17600,
                "end_ms": 26500,
                "text_en": "Thank you. Here is your temporary guest badge. Please wear it visibly at all times while inside the building.",
                "text_tr": "Teşekkürler. İşte geçici misafir kartınız. Bina içindeyken lütfen kartı her zaman görünür şekilde takın."
            },
            {
                "index": 5,
                "speaker_id": "mark",
                "start_ms": 26900,
                "end_ms": 34800,
                "text_en": "I will pin it to my jacket right now. Should I take the elevator up to the fourth floor?",
                "text_tr": "Hemen şimdi ceketimin üzerine takıyorum. Dördüncü kata asansörle mi çıkmalıyım?"
            },
            {
                "index": 6,
                "speaker_id": "ece",
                "start_ms": 35200,
                "end_ms": 45000,
                "text_en": "Actually, conference room four is on the second floor. Karen is on her way down to greet you in the lobby.",
                "text_tr": "Aslında dört numaralı konferans salonu ikinci katta. Karen sizi lobide karşılamak için aşağıya iniyor."
            }
        ],
        "key_vocabulary": [
            {
                "word": "professional",
                "vocab_id": "vocab.professional",
                "context_note_tr": "Misafir karşılama ve kurumsal güvenlik kurallarına uyum."
            },
            {
                "word": "office",
                "vocab_id": "vocab.office",
                "context_note_tr": "Şirket genel merkezi ve resepsiyon alanı."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_a2_visit_01",
                "What organization does Mark represent?",
                "Mark hangi kuruluşu temsil etmektedir?",
                "SecureNet Consulting",
                [
                    "Sunrise Catering Bakery",
                    "City Public Transportation Authority",
                    "Global Airport Logistics"
                ],
                "Mark introduces himself: 'My name is Mark Vance from SecureNet Consulting.'",
                "Mark SecureNet Danışmanlık'tan geldiğini söyler."
            ),
            build_q(
                "q_a2_visit_02",
                "What document does Mark provide for visitor registration?",
                "Mark ziyaretçi kaydı için hangi belgeyi sunmaktadır?",
                "His driver's license",
                [
                    "A handwritten utility electricity bill",
                    "His university diploma certificate",
                    "A library membership borrowing card"
                ],
                "Mark says: 'Here is my driver's license.'",
                "Mark sürücü belgesini sunduğunu ifade eder."
            ),
            build_q(
                "q_a2_visit_03",
                "What security instruction does Ece give Mark regarding his badge?",
                "Ece kartıyla ilgili Mark'a hangi güvenlik talimatını vermektedir?",
                "To wear it visibly at all times while inside the building",
                [
                    "To hide it inside his shoes whenever walking past offices",
                    "To return it to the local police department after thirty minutes",
                    "To photocopy it fifty times on the corridor printer"
                ],
                "Ece instructs: 'Please wear it visibly at all times while inside the building.'",
                "Ece bina içindeyken kartı her zaman görünür şekilde takması talimatını verir."
            ),
            build_q(
                "q_a2_visit_04",
                "On which floor is conference room four actually located?",
                "Dört numaralı konferans salonu gerçekte hangi katta bulunmaktadır?",
                "On the second floor",
                [
                    "On the fourth floor",
                    "On the rooftop observation platform",
                    "In the sub-basement boiler room"
                ],
                "Ece clarifies: 'Actually, conference room four is on the second floor.'",
                "Ece aslında dört numaralı salonun ikinci katta olduğunu belirtir."
            ),
            build_q(
                "q_a2_visit_05",
                "Where will Karen meet Mark?",
                "Karen Mark ile nerede buluşacaktır?",
                "She is coming down to greet him in the lobby",
                [
                    "At a cafeteria three blocks away from headquarters",
                    "In the underground subway station across the street",
                    "Inside the executive parking garage elevator"
                ],
                "Ece says: 'Karen is on her way down to greet you in the lobby.'",
                "Ece Karen'ın kendisini karşılamak için lobiye inmekte olduğunu söyler."
            )
        ],
        "topic_tags": ["reception", "security", "visitor-management", "workplace-communication"],
        "related_ids": ["vocab.professional", "vocab.office"],
        "status": "APPROVED",
        "version": 1
    }
]

if __name__ == "__main__":
    print(f"Generated {len(A2_LISTENING_SCENARIOS)} A2 listening scenarios.")
    for s in A2_LISTENING_SCENARIOS:
        print(f"  [{s['cefr_level']}] {s['id']} - {s['title']} ({len(s['transcript_items'])} items, {len(s['comprehension_questions'])} questions)")
