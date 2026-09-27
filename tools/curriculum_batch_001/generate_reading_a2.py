#!/usr/bin/env python3
"""
Reading Generator for A2 (8 articles) and B1 (8 articles).
Each article has complete English and Turkish paragraphs, vocabulary annotations,
and 5 comprehension questions balanced by McqBalancer.
"""

from mcq_balancer import McqBalancer

balancer = McqBalancer(seed=101)

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

A2_READING_ARTICLES = [
    {
        "id": "reading.a2.remote-work-basics",
        "title": "Setting Up an Effective Home Office",
        "cefr_level": "A2",
        "category": "workplace_communication",
        "summary_en": "Practical advice for remote workers on organizing a productive desk space, managing digital notifications, and establishing healthy work hours.",
        "summary_tr": "Uzaktan çalışanlar için verimli bir çalışma alanı düzenleme, bildirimleri yönetme ve sağlıklı çalışma saatleri belirleme rehberi.",
        "word_count": 315,
        "estimated_reading_minutes": 2,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "Choosing the Right Work Area",
                "content_en": "Working from home requires a clear boundary between personal life and daily professional duties. Many remote employees make the mistake of working from the couch or bed, which reduces focus and causes back pain. Setting up a dedicated desk with an ergonomic chair and adequate natural light helps your brain switch into work mode quickly every morning.",
                "content_tr": "Evden çalışmak, kişisel yaşam ile günlük mesleki görevler arasında net bir sınır gerektirir. Birçok uzaktan çalışan, odaklanmayı azaltan ve sırt ağrısına neden olan kanepede veya yatakta çalışma hatasına düşer. Ergonomik bir sandalye ve yeterli doğal ışık alan özel bir masa kurmak, beyninizin her sabah hızla çalışma moduna geçmesine yardımcı olur."
            },
            {
                "paragraph_index": 2,
                "title": "Managing Digital Distractions",
                "content_en": "Digital clutter can be just as harmful as physical mess. Continuous notifications from messaging channels, emails, and social media break your concentration throughout the day. To protect your productivity, turn off non-essential notifications during focus blocks. Check team updates at scheduled times rather than responding instantly to every single alert.",
                "content_tr": "Dijital karmaşa, fiziksel dağınıklık kadar zararlı olabilir. Mesajlaşma kanallarından, e-postalardan ve sosyal medyadan gelen sürekli bildirimler gün boyunca konsantrasyonunuzu bozar. Üretkenliğinizi korumak için odaklanma blokları sırasında zorunlu olmayan bildirimleri kapatın. Her uyarıya anında yanıt vermek yerine ekip güncellemelerini planlanan zamanlarda kontrol edin."
            },
            {
                "paragraph_index": 3,
                "title": "Ending the Workday on Time",
                "content_en": "One of the greatest challenges of remote work is knowing when to stop. When your office is inside your home, work hours can easily expand into late evenings. Establish a clear shutdown ritual, such as reviewing your calendar for tomorrow and closing your laptop. This routine signals to your mind that the workday is officially complete.",
                "content_tr": "Uzaktan çalışmanın en büyük zorluklarından biri ne zaman duracağını bilmektir. Ofisiniz evinizin içinde olduğunda, çalışma saatleri kolayca akşamın geç saatlerine uzayabilir. Yarının takvimini gözden geçirmek ve dizüstü bilgisayarınızı kapatmak gibi net bir mesai bitirme ritüeli oluşturun. Bu rutin zihninize iş gününün resmen tamamlandığını bildirir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "boundary",
                "vocab_id": "vocab.boundary",
                "context_definition_en": "A real or conceptual dividing line between two different spaces or activities.",
                "context_meaning_tr": "İki alan veya faaliyet arasındaki sınır çizgisi."
            },
            {
                "word": "productivity",
                "vocab_id": "vocab.productivity",
                "context_definition_en": "The rate and efficiency with which useful work is completed.",
                "context_meaning_tr": "Yararlı işlerin üretilme hızı ve verimliliği."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_a2_01_01",
                "What is a major problem with working from the couch or bed?",
                "Kanepede veya yatakta çalışmanın ana sorunu nedir?",
                "It reduces mental concentration and can cause physical discomfort",
                ["It requires an expensive commercial software license", "It speeds up laptop battery consumption too quickly", "It prevents you from receiving internet emails"],
                "Paragraph 1 explicitly notes that working from bed reduces focus and causes back pain.",
                "1. paragraf yatakta veya kanepede çalışmanın odaklanmayı azalttığını ve sırt ağrısına yol açtığını belirtir."
            ),
            build_q(
                "q_r_a2_01_02",
                "How does the text recommend handling digital notifications?",
                "Metin dijital bildirimlerin nasıl ele alınmasını önermektedir?",
                "By disabling non-essential alerts during designated focus periods",
                ["By buying three separate smartphones for each project", "By answering every chat message in under ten seconds", "By never checking email at any point during the week"],
                "Paragraph 2 recommends turning off non-essential notifications during focus blocks.",
                "2. paragraf odaklanma bloklarında zorunlu olmayan bildirimleri kapatmayı önerir."
            ),
            build_q(
                "q_r_a2_01_03",
                "Why can work easily expand into late evenings for remote staff?",
                "Uzaktan çalışanlar için mesai neden kolayca geç saatlere sarkabilir?",
                "Because the physical boundary between home and office is removed",
                ["Because company servers only operate after midnight", "Because managers require continuous phone calls all night", "Because electricity is cheaper during the late evening"],
                "Paragraph 3 explains that when your office is in your home, work hours expand easily.",
                "3. paragraf ofis evde olduğunda çalışma saatlerinin kolayca uzayabildiğini açıklar."
            ),
            build_q(
                "q_r_a2_01_04",
                "What is the intended purpose of an end-of-day shutdown routine?",
                "Mesai bitirme rutininin asıl amacı nedir?",
                "To psychologically signal that professional tasks have ended",
                ["To automatically delete all completed work documents", "To report teammates who failed to finish their assignments", "To prepare breakfast for the following morning"],
                "The text states that the ritual signals to the mind that the workday is officially complete.",
                "Metin bu ritüelin zihne iş gününün bittiğini bildirdiğini ifade eder."
            ),
            build_q(
                "q_r_a2_01_05",
                "Which adjective best describes the overall tone of this article?",
                "Bu makalenin genel tonunu en iyi hangi sıfat tanımlar?",
                "Practical and supportive",
                ["Aggressive and demanding", "Fictional and humorous", "Mysterious and confusing"],
                "The article offers helpful, practical workplace advice in an encouraging manner.",
                "Makale teşvik edici ve pratik iş yeri tavsiyeleri sunar."
            )
        ],
        "topic_tags": ["remote-work", "productivity", "workplace-habits", "a2-reading"],
        "related_ids": ["vocab.schedule", "vocab.boundary"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.a2.customer-email-etiquette",
        "title": "Writing Polite Professional Emails to International Clients",
        "cefr_level": "A2",
        "category": "workplace_communication",
        "summary_en": "Essential guidelines for composing clear, respectful emails in English, including greeting conventions, clear calls to action, and courteous closing sign-offs.",
        "summary_tr": "Uluslararası müşterilere açık ve saygılı e-postalar yazma rehberi: selamlama kuralları, net eylem çağrıları ve kibar kapanış kalıpları.",
        "word_count": 320,
        "estimated_reading_minutes": 2,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "First Impressions and Greetings",
                "content_en": "In modern international business, the tone of your opening sentence establishes the relationship with your client. Using overly informal greetings like 'Hey' can sound unprofessional to new business partners. Instead, use 'Dear Mr. Smith' or 'Dear Alex' depending on your familiarity with the person. If you are writing to a group, 'Dear Team' or 'Hello everyone' is both friendly and appropriate.",
                "content_tr": "Modern uluslararası iş dünyasında, açılış cümlenizin tonu müşterinizle olan ilişkinizi belirler. 'Hey' gibi aşırı samimi selamlamalar kullanmak yeni iş ortaklarına gayri profesyonel gelebilir. Bunun yerine tanışıklık derecenize göre 'Dear Mr. Smith' veya 'Dear Alex' kullanın. Bir gruba yazıyorsanız 'Dear Team' veya 'Hello everyone' hem samimi hem uygundur."
            },
            {
                "paragraph_index": 2,
                "title": "Clarity in the Message Body",
                "content_en": "Business professionals receive dozens of emails every day, so long paragraphs are often skipped. State the primary purpose of your message in the very first or second sentence. If you are requesting an action or sharing feedback, use concise bullet points. This visual structure allows the client to scan the information quickly and understand what you need.",
                "content_tr": "İş insanları her gün düzinelerce e-posta alır, bu nedenle uzun paragraflar genellikle atlanır. Mesajınızın asıl amacını ilk veya ikinci cümlede belirtin. Bir eylem talep ediyorsanız veya geri bildirim paylaşıyorsanız kısa madde işaretleri kullanın. Bu görsel yapı, müşterinin bilgileri hızla taramasına ve neye ihtiyacınız olduğunu anlamasına olanak tanır."
            },
            {
                "paragraph_index": 3,
                "title": "Courteous Closing and Next Steps",
                "content_en": "Always conclude your message with a polite sign-off and a clear expectation for next steps. Phrases like 'Please let me know if you have any questions' show willingness to support the client. Finish with standard business closings such as 'Best regards' or 'Sincerely' followed by your full name, job title, and company contact details.",
                "content_tr": "Mesajınızı daima kibar bir kapanış ve sonraki adımlara dair net bir beklentiyle sonlandırın. 'Please let me know if you have any questions' gibi ifadeler müşteriye destek olma isteğinizi gösterir. 'Best regards' veya 'Sincerely' gibi standart iş kapanışlarının ardından tam adınız, unvanınız ve şirket iletişim bilgilerinizle bitirin."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "etiquette",
                "vocab_id": "vocab.etiquette",
                "context_definition_en": "The customary code of polite behavior in society or among members of a profession.",
                "context_meaning_tr": "İş veya toplum hayatında kabul gören nezaket ve görgü kuralları."
            },
            {
                "word": "concise",
                "vocab_id": "vocab.concise",
                "context_definition_en": "Giving a lot of information clearly and in a few words.",
                "context_meaning_tr": "Kısa, öz ve net ifade edilmiş."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_a2_02_01",
                "Why should you avoid opening a formal business email with 'Hey'?",
                "Resmi bir iş e-postasına 'Hey' ile başlamaktan neden kaçınılmalıdır?",
                "It can sound excessively informal and unprofessional to new clients",
                ["It triggers automated email spam filters immediately", "It is considered illegal in international maritime trade", "It prevents attachments from uploading properly"],
                "Paragraph 1 notes that 'Hey' can sound unprofessional to new business partners.",
                "1. paragraf 'Hey' ifadesinin yeni iş ortaklarına gayri profesyonel gelebileceğini belirtir."
            ),
            build_q(
                "q_r_a2_02_02",
                "Where should the main purpose of your email be stated?",
                "E-postanızın ana amacı nerede belirtilmelidir?",
                "In the very first or second sentence of the message body",
                ["At the very end of the email signature block", "In an encrypted zip attachment file", "Only after five introductory paragraphs"],
                "Paragraph 2 states that the primary purpose should appear in the first or second sentence.",
                "2. paragraf ana amacın ilk veya ikinci cümlede belirtilmesi gerektiğini söyler."
            ),
            build_q(
                "q_r_a2_02_03",
                "How does using bullet points help international readers?",
                "Madde işaretleri kullanmak uluslararası okuyuculara nasıl yardımcı olur?",
                "It organizes information visually so it can be scanned quickly",
                ["It translates English text into Turkish automatically", "It guarantees that the email will never be deleted", "It allows you to send emails without an internet connection"],
                "Paragraph 2 explains that bullet points allow the client to scan information quickly.",
                "2. paragraf madde işaretlerinin müşterinin bilgiyi hızla taramasına imkan verdiğini açıklar."
            ),
            build_q(
                "q_r_a2_02_04",
                "Which phrase is recommended for offering continued assistance?",
                "Sürekli yardım sunmak için hangi ifade önerilmektedir?",
                "Please let me know if you have any questions",
                ["Do not reply to this email under any circumstances", "I will never answer any inquiries about this project", "You must accept this contract without reading it"],
                "Paragraph 3 mentions 'Please let me know if you have any questions' as a courteous phrase.",
                "3. paragraf nezaketi göstermek için bu kalıbı tavsiye eder."
            ),
            build_q(
                "q_r_a2_02_05",
                "What information should follow 'Best regards' in a professional sign-off?",
                "Profesyonel bir kapanışta 'Best regards' sonrasında ne yer almalıdır?",
                "Your full name, job title, and company contact details",
                ["A list of your personal social media passwords", "A complaint about your company's management", "A detailed weather report for your city"],
                "The text advises finishing with your full name, job title, and contact details.",
                "Metin tam adınız, unvanınız ve iletişim bilgilerinizle bitirmeyi önerir."
            )
        ],
        "topic_tags": ["business-writing", "email-etiquette", "workplace-communication", "a2-reading"],
        "related_ids": ["vocab.etiquette", "vocab.concise"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.a2.cloud-storage-guide",
        "title": "How Cloud Storage Protects Everyday Workplace Documents",
        "cefr_level": "A2",
        "category": "technology",
        "summary_en": "An accessible introduction to cloud storage systems, synchronization across devices, automated version backups, and remote team access.",
        "summary_tr": "Bulut depolama sistemleri, cihazlar arası eşitleme, otomatik sürüm yedekleme ve uzaktan ekip erişimine giriş rehberi.",
        "word_count": 305,
        "estimated_reading_minutes": 2,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "What is Cloud Storage?",
                "content_en": "In the past, office workers saved important spreadsheets and presentations onto physical USB drives or local hard drives. If a computer was lost or damaged, crucial files were permanently gone. Cloud storage solves this vulnerability by saving your documents onto secure remote data centers managed by trusted technology providers.",
                "content_tr": "Geçmişte ofis çalışanları önemli tabloları ve sunumları fiziksel USB disklere veya yerel sabit disklere kaydederdi. Bir bilgisayar kaybolduğunda veya hasar gördüğünde önemli dosyalar kalıcı olarak yok olurdu. Bulut depolama, belgelerinizi güvenilir teknoloji sağlayıcıları tarafından yönetilen uzak veri merkezlerine kaydederek bu açığı çözer."
            },
            {
                "paragraph_index": 2,
                "title": "Automatic Synchronization Across Devices",
                "content_en": "One of the most valuable benefits of the cloud is automatic file synchronization. When you edit a document on your office computer, the changes update immediately on your home laptop and smartphone. You no longer need to email different file versions like 'report_final_v2.docx' to yourself or your colleagues.",
                "content_tr": "Bulutun en değerli faydalarından biri otomatik dosya eşitlemesidir. Ofis bilgisayarınızda bir belgeyi düzenlediğinizde, değişiklikler evdeki dizüstü bilgisayarınızda ve akıllı telefonunuzda anında güncellenir. Artık 'rapor_son_v2.docx' gibi farklı dosya sürümlerini kendinize veya iş arkadaşlarınıza e-posta ile göndermenize gerek kalmaz."
            },
            {
                "paragraph_index": 3,
                "title": "Version History and Safe Recovery",
                "content_en": "Modern cloud platforms also maintain an automated version history for every file. If someone accidentally deletes an important paragraph or modifies a complex formula, you can restore previous versions with a single click. This safety feature gives engineering and finance teams peace of mind during rapid daily updates.",
                "content_tr": "Modern bulut platformları ayrıca her dosya için otomatik bir sürüm geçmişi tutar. Biri yanlışlıkla önemli bir paragrafı silerse veya karmaşık bir formülü değiştirirse, önceki sürümleri tek bir tıklamayla geri yükleyebilirsiniz. Bu güvenlik özelliği, hızlı günlük güncellemeler sırasında mühendislik ve finans ekiplerine huzur verir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "synchronization",
                "vocab_id": "vocab.synchronization",
                "context_definition_en": "The process of making data identical across multiple devices or platforms.",
                "context_meaning_tr": "Verilerin birden fazla cihazda eş zamanlı ve aynı hale getirilmesi süreci."
            },
            {
                "word": "restore",
                "vocab_id": "vocab.restore",
                "context_definition_en": "To bring a file back to its former or original condition.",
                "context_meaning_tr": "Bir dosyayı önceki veya orijinal durumuna geri döndürmek."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_a2_03_01",
                "What was a primary risk of relying on local hard drives in the past?",
                "Geçmişte yerel sabit disklere güvenmenin ana riski neydi?",
                "Hardware damage could permanently destroy important business files",
                ["Hard drives required continuous human voice input to operate", "Computers could only save text files written in Latin", "Local disks were strictly forbidden by international law"],
                "Paragraph 1 notes that if a computer was lost or damaged, crucial files were gone.",
                "1. paragraf bilgisayar hasar gördüğünde dosyaların kalıcı olarak yok olduğunu belirtir."
            ),
            build_q(
                "q_r_a2_03_02",
                "How does automatic file synchronization improve daily collaboration?",
                "Otomatik dosya eşitlemesi günlük iş birliğini nasıl geliştirir?",
                "Changes update across all connected devices without emailing attachments",
                ["It prints physical copies of every document automatically", "It limits file editing to only one person per month", "It forces employees to work only from the office"],
                "Paragraph 2 states that changes update immediately across devices without emailing versions.",
                "2. paragraf değişikliklerin e-posta göndermeden tüm cihazlarda güncellendiğini açıklar."
            ),
            build_q(
                "q_r_a2_03_03",
                "What does cloud 'version history' allow users to do?",
                "Bulut 'sürüm geçmişi' kullanıcıların ne yapmasına imkan tanır?",
                "Recover previously saved copies of a document if mistakes happen",
                ["Translate any document into forty languages instantly", "Delete all previous files from the entire internet permanently", "Monitor employee keystrokes without their permission"],
                "Paragraph 3 explains that version history allows you to restore previous versions with one click.",
                "3. paragraf sürüm geçmişinin önceki versiyonları tek tıkla geri yüklemeyi sağladığını belirtir."
            ),
            build_q(
                "q_r_a2_03_04",
                "Where are cloud files physically stored according to the passage?",
                "Metne göre bulut dosyaları fiziksel olarak nerede saklanır?",
                "In secure remote data centers managed by technology providers",
                ["Inside the user's home computer keyboard", "On local paper records stored in underground vaults", "In satellites that only orbit over European airspace"],
                "The text mentions that documents are saved in secure remote data centers.",
                "Metin belgelerin güvenli uzak veri merkezlerinde saklandığını ifade eder."
            ),
            build_q(
                "q_r_a2_03_05",
                "Which teams benefit especially from version recovery during daily work?",
                "Günlük çalışma sırasında sürüm kurtarmadan özellikle hangi ekipler yararlanır?",
                "Engineering and finance teams making rapid regular updates",
                ["Teams that never use computers or digital spreadsheets", "Outdoor sports coaches during summer athletic events", "Aircraft pilots during routine ocean crossings"],
                "Paragraph 3 specifically mentions engineering and finance teams.",
                "3. paragraf özellikle mühendislik ve finans ekiplerini örnek verir."
            )
        ],
        "topic_tags": ["cloud-storage", "technology", "data-safety", "a2-reading"],
        "related_ids": ["vocab.synchronization", "vocab.restore"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.a2.daily-standup-guide",
        "title": "Why Daily Standup Meetings Keep Teams Aligned",
        "cefr_level": "A2",
        "category": "engineering_culture",
        "summary_en": "An overview of the fifteen-minute daily standup ritual in modern agile teams, focusing on yesterday's progress, today's goals, and removing blockers.",
        "summary_tr": "Modern çevik ekiplerde 15 dakikalık günlük standup toplantısının işleyişi: dünün ilerlemesi, bugünün hedefleri ve engellerin kaldırılması.",
        "word_count": 290,
        "estimated_reading_minutes": 2,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Purpose of a Quick Morning Alignment",
                "content_en": "In many modern companies, engineering and product teams start each workday with a fifteen-minute meeting called the daily standup. The goal is not to deliver long managerial lectures, but to share a quick synchronization. Participants literally stand up in office settings, which encourages everyone to keep their updates concise and focused.",
                "content_tr": "Birçok modern şirkette mühendislik ve ürün ekipleri her iş gününe günlük standup adı verilen 15 dakikalık bir toplantıyla başlar. Amaç uzun yönetim dersleri vermek değil, hızlı bir eşzamanlama paylaşmaktır. Katılımcılar ofis ortamında kelimenin tam anlamıyla ayağa kalkarlar, bu da herkesin güncellemelerini kısa ve odaklı tutmasını teşvik eder."
            },
            {
                "paragraph_index": 2,
                "title": "The Three Core Questions",
                "content_en": "During the standup, each team member briefly answers three fundamental questions. First, what tasks did you complete yesterday? Second, what are you planning to work on today? Third, is there any blocker preventing you from finishing your work? By keeping the conversation strictly on these points, the meeting finishes on time.",
                "content_tr": "Standup sırasında her ekip üyesi üç temel soruyu kısaca yanıtlar. Birincisi, dün hangi görevleri tamamladınız? İkincisi, bugün ne üzerinde çalışmayı planlıyorsunuz? Üçüncüsü, işinizi bitirmenizi engelleyen herhangi bir engel var mı? Konuşmayı kesinlikle bu noktalar üzerinde tutarak toplantı zamanında biter."
            },
            {
                "paragraph_index": 3,
                "title": "Focusing on Blockers Rather than Blame",
                "content_en": "The most critical part of the standup is identifying impediments early. If a developer is waiting for a database password or design asset, the team lead steps in immediately to assist. Standups create a supportive culture where asking for help is valued, ensuring small problems do not become major project delays.",
                "content_tr": "Standup toplantısının en kritik kısmı, engelleri erkenden tespit etmektir. Bir yazılımcı bir veritabanı şifresi veya tasarım dosyası bekliyorsa, ekip lideri yardım etmek için hemen devreye girer. Standup'lar yardım istemenin değerli görüldüğü destekleyici bir kültür yaratarak küçük sorunların büyük proje gecikmelerine dönüşmesini önler."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "alignment",
                "vocab_id": "vocab.alignment",
                "context_definition_en": "Agreement and coordinated direction among members of a group.",
                "context_meaning_tr": "Bir grubun üyeleri arasında ortak vizyon ve uyum."
            },
            {
                "word": "impediment",
                "vocab_id": "vocab.impediment",
                "context_definition_en": "A hindrance, obstruction, or blocker that slows down progress.",
                "context_meaning_tr": "İlerlemeyi yavaşlatan engel veya aksaklık."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_a2_04_01",
                "Why do participants physically stand up during in-person standups?",
                "Katılımcılar yüz yüze toplantılarda neden fiziksel olarak ayağa kalkarlar?",
                "It encourages people to keep their speaking updates brief and concise",
                ["It is a mandatory physical fitness test required by company health insurance", "It helps the office cameras record facial expressions more clearly", "There are never enough chairs in corporate conference rooms"],
                "Paragraph 1 notes that standing up encourages everyone to keep updates concise.",
                "1. paragraf ayakta durmanın konuşmaları kısa ve öz tutmayı teşvik ettiğini belirtir."
            ),
            build_q(
                "q_r_a2_04_02",
                "Which of the following is NOT one of the three core standup questions?",
                "Aşağıdakilerden hangisi üç temel standup sorusundan biri DEĞİLDİR?",
                "How much money did the company make in the stock market today?",
                ["What tasks did you complete yesterday?", "What are you planning to work on today?", "Is there any blocker preventing you from finishing your work?"],
                "The three questions cover yesterday's work, today's plan, and current blockers.",
                "Üç soru dünün işini, bugünün planını ve engelleri kapsar; borsa kazancı bunlardan biri değildir."
            ),
            build_q(
                "q_r_a2_04_03",
                "What is considered the most critical objective of the standup?",
                "Standup toplantısının en kritik hedefi ne olarak kabul edilir?",
                "Detecting technical impediments early before they cause project delays",
                ["Disciplining employees who arrived late to the morning meeting", "Reviewing detailed monthly financial spreadsheets line by line", "Electing a new chief executive officer every morning"],
                "Paragraph 3 explicitly states that identifying impediments early is the most critical part.",
                "3. paragraf engelleri erkenden tespit etmenin en kritik kısım olduğunu açıkça belirtir."
            ),
            build_q(
                "q_r_a2_04_04",
                "What does the team lead do when a blocker is reported?",
                "Bir engel bildirildiğinde ekip lideri ne yapar?",
                "Intervenes promptly to help resolve the impediment",
                ["Cancels the entire project and closes the company office", "Fines the engineer fifty dollars for asking for assistance", "Ignores the issue until the end of the fiscal quarter"],
                "The text states the team lead steps in immediately to assist.",
                "Metin ekip liderinin yardım etmek için derhal devreye girdiğini ifade eder."
            ),
            build_q(
                "q_r_a2_04_05",
                "How long does a standard daily standup meeting typically last?",
                "Standart bir günlük standup toplantısı genellikle ne kadar sürer?",
                "Approximately fifteen minutes",
                ["At least three hours", "Two full business days", "Exactly thirty seconds"],
                "Paragraph 1 mentions a fifteen-minute meeting.",
                "1. paragraf toplantının 15 dakika sürdüğünü belirtir."
            )
        ],
        "topic_tags": ["agile", "standup", "teamwork", "engineering-culture", "a2-reading"],
        "related_ids": ["vocab.alignment", "vocab.impediment"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.a2.healthy-desk-habits",
        "title": "Ergonomics and Well-Being at the Office Desk",
        "cefr_level": "A2",
        "category": "workplace_communication",
        "summary_en": "Simple ergonomic adjustments and micro-break routines to prevent physical fatigue, eye strain, and back discomfort during long desk hours.",
        "summary_tr": "Uzun masa başı mesailerinde fiziksel yorgunluğu, göz yorgunluğunu ve sırt ağrısını önlemek için ergonomik ayarlar ve mikro mola rutinleri.",
        "word_count": 310,
        "estimated_reading_minutes": 2,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Dangers of Prolonged Sitting",
                "content_en": "Many knowledge workers spend eight or more hours every day sitting in front of a computer screen. Without proper posture and ergonomic equipment, prolonged sitting frequently leads to chronic neck stiffness, wrist pain, and lower back tension. Fortunately, small adjustments to your chair and monitor position can significantly improve your physical comfort.",
                "content_tr": "Birçok bilgi işçisi her gün bir bilgisayar ekranı karşısında oturarak sekiz veya daha fazla saat geçirir. Uygun duruş ve ergonomik ekipman olmadan, uzun süreli oturma sıklıkla kronik boyun tutulmasına, bilek ağrısına ve bel gerginliğine yol açar. Neyse ki sandalyenize ve monitör konumunuza yapacağınız küçük ayarlamalar fiziksel konforunuzu önemli ölçüde artırabilir."
            },
            {
                "paragraph_index": 2,
                "title": "Setting Up an Ergonomic Workstation",
                "content_en": "Begin by adjusting your chair height so that your feet rest flat on the floor and your knees form a ninety-degree angle. Your computer monitor should be positioned directly in front of you at arm's length, with the top of the display aligned with your eye level. This setup prevents you from tilting your head forward or hunching your shoulders.",
                "content_tr": "Ayaklarınızın yere düz basması ve dizlerinizin doksan derecelik bir açı oluşturması için sandalye yüksekliğinizi ayarlayarak başlayın. Bilgisayar monitörünüz, ekranın üst kısmı göz hizanızla aynı hizada olacak şekilde, kol mesafesinde doğrudan önünüze yerleştirilmelidir. Bu kurulum başınızı öne eğmenizi veya omuzlarınızı kamburlaştırmanızı önler."
            },
            {
                "paragraph_index": 3,
                "title": "The Power of Regular Micro-Breaks",
                "content_en": "Even with an ideal ergonomic desk, the human body needs regular movement. Follow the 20-20-20 rule to protect your vision: every twenty minutes, look at an object twenty feet away for at least twenty seconds. Stand up, stretch your arms, and drink a glass of water every hour to keep your circulation active and your mind refreshed.",
                "content_tr": "İdeal bir ergonomik masada bile insan vücudunun düzenli harekete ihtiyacı vardır. Gözlerinizi korumak için 20-20-20 kuralını uygulayın: her yirmi dakikada bir, en az yirmi saniye boyunca yirmi fit (yaklaşık 6 metre) uzaktaki bir nesneye bakın. Dolaşımınızı aktif ve zihninizi taze tutmak için her saat başı ayağa kalkın, kollarınızı esnetin ve bir bardak su için."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "posture",
                "vocab_id": "vocab.posture",
                "context_definition_en": "The position in which someone holds their body when standing or sitting.",
                "context_meaning_tr": "Otururken veya ayaktayken vücudun duruş biçimi."
            },
            {
                "word": "fatigue",
                "vocab_id": "vocab.fatigue",
                "context_definition_en": "Extreme physical or mental tiredness resulting from effort or stress.",
                "context_meaning_tr": "Aşırı yorgunluk ve bitkinlik durumu."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_a2_05_01",
                "What physical problems commonly result from prolonged improper sitting?",
                "Uzun süre hatalı oturmaktan yaygın olarak hangi fiziksel sorunlar kaynaklanır?",
                "Neck stiffness, wrist discomfort, and lower back tension",
                ["Instant loss of speech and total memory amnesia", "Severe allergic reactions to fresh fruit and vegetables", "Permanent discoloration of the fingernails"],
                "Paragraph 1 lists neck stiffness, wrist pain, and lower back tension.",
                "1. paragraf boyun tutulması, bilek ağrısı ve bel gerginliğini sıralar."
            ),
            build_q(
                "q_r_a2_05_02",
                "How should a computer monitor be positioned relative to your eyes?",
                "Bilgisayar monitörü gözlerinize göre nasıl konumlandırılmalıdır?",
                "At arm's length with the top edge aligned with eye level",
                ["Resting on the floor tilted upwards toward the ceiling", "Directly against your nose for maximum visual magnification", "Hidden behind books to minimize screen brightness"],
                "Paragraph 2 states the monitor should be at arm's length, top aligned with eye level.",
                "2. paragraf monitörün kol mesafesinde ve üst kenarının göz hizasında olması gerektiğini belirtir."
            ),
            build_q(
                "q_r_a2_05_03",
                "What is the recommended 20-20-20 rule designed to protect?",
                "Önerilen 20-20-20 kuralı neyi korumak için tasarlanmıştır?",
                "Your eyesight and visual comfort during screen time",
                ["Your company laptop from overheating during gaming", "Your office chair fabric from wearing out too quickly", "Your financial bank account from internet fraud"],
                "Paragraph 3 explains that the 20-20-20 rule protects vision.",
                "3. paragraf bu kuralın göz sağlığını koruduğunu açıklar."
            ),
            build_q(
                "q_r_a2_05_04",
                "How often does the article recommend standing up and moving?",
                "Makale ne sıklıkla ayağa kalkıp hareket etmeyi önermektedir?",
                "Every hour for brief stretching and hydration",
                ["Only once at the end of each calendar month", "Never, because sitting continuously burns more calories", "Every thirty seconds without ever sitting down"],
                "The text advises standing up, stretching, and drinking water every hour.",
                "Metin her saat başı ayağa kalkıp esnemeyi ve su içmeyi tavsiye eder."
            ),
            build_q(
                "q_r_a2_05_05",
                "How should your feet be placed when your desk chair is adjusted properly?",
                "Çalışma koltuğu düzgün ayarlandığında ayaklarınız nasıl durmalıdır?",
                "Resting flat on the floor with knees at a ninety-degree angle",
                ["Crossed tightly behind the chair wheels", "Elevated high above the desk surface", "Dangling without touching any surface"],
                "Paragraph 2 states feet should rest flat on the floor with knees at ninety degrees.",
                "2. paragraf ayakların yere düz basması ve dizlerin 90 derece olması gerektiğini belirtir."
            )
        ],
        "topic_tags": ["ergonomics", "health", "workplace-habits", "a2-reading"],
        "related_ids": ["vocab.posture", "vocab.fatigue"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.a2.budgeting-starter-guide",
        "title": "Simple Budgeting Techniques for New Technology Projects",
        "cefr_level": "A2",
        "category": "finance_and_economics",
        "summary_en": "A beginner's guide to tracking direct software costs, hardware subscriptions, labor hours, and building realistic contingency reserves for small projects.",
        "summary_tr": "Yeni projeler için doğrudan yazılım maliyetlerini, donanım aboneliklerini, çalışma saatlerini izleme ve beklenmedik durum payı oluşturma rehberi.",
        "word_count": 310,
        "estimated_reading_minutes": 2,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "Why Every Project Needs a Budget",
                "content_en": "Starting an exciting new software project is rewarding, but technical passion without financial discipline leads to premature failure. Without a clear budget, small expenses like cloud hosting fees, software licenses, and contractor hours accumulate rapidly. Creating an initial cost estimate ensures your team has enough capital to finish what you start.",
                "content_tr": "Heyecan verici yeni bir yazılım projesine başlamak tatmin edicidir, ancak finansal disiplin olmadan teknik tutku erken başarısızlığa yol açar. Net bir bütçe olmadan bulut barındırma ücretleri, yazılım lisansları ve yüklenici saatleri gibi küçük harcamalar hızla birikir. Başlangıç maliyet tahmini oluşturmak, ekibinizin başladığı işi bitirmek için yeterli sermayeye sahip olmasını sağlar."
            },
            {
                "paragraph_index": 2,
                "title": "Categorizing Direct and Indirect Costs",
                "content_en": "To organize your project expenses, separate costs into two main categories: fixed direct costs and variable expenses. Direct costs include monthly server subscriptions and required developer software tools. Variable expenses fluctuate based on workload, such as freelance design help or promotional digital marketing campaigns.",
                "content_tr": "Proje harcamalarınızı düzenlemek için maliyetleri iki ana kategoriye ayırın: sabit doğrudan maliyetler ve değişken giderler. Doğrudan maliyetler aylık sunucu aboneliklerini ve gerekli yazılımcı araçlarını içerir. Değişken harcamalar serbest zamanlı tasarım yardımı veya dijital tanıtım kampanyaları gibi iş yüküne göre dalgalanır."
            },
            {
                "paragraph_index": 3,
                "title": "Building a Contingency Buffer",
                "content_en": "Experienced project managers know that unexpected challenges always occur. An API provider might increase its pricing, or unexpected security testing might require extra hours. Always add a ten to fifteen percent contingency reserve to your overall budget. This financial cushion prevents minor surprises from halting your project.",
                "content_tr": "Deneyimli proje yöneticileri beklenmedik zorlukların her zaman ortaya çıkacağını bilir. Bir API sağlayıcısı fiyatlarını artırabilir veya beklenmedik güvenlik testleri fazladan saatler gerektirebilir. Genel bütçenize her zaman yüzde on ila on beşlik bir beklenmedik durum payı (yedek akçe) ekleyin. Bu finansal tampon, küçük sürprizlerin projenizi durdurmasını önler."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "contingency",
                "vocab_id": "vocab.contingency",
                "context_definition_en": "A provision for an unforeseen event or future emergency.",
                "context_meaning_tr": "Öngörülemeyen bir durum veya acil hal için ayrılan pay."
            },
            {
                "word": "capital",
                "vocab_id": "vocab.capital",
                "context_definition_en": "Financial assets or money available to fund a project or enterprise.",
                "context_meaning_tr": "Bir projeyi veya işletmeyi finanse etmek için mevcut para veya sermaye."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_a2_06_01",
                "What often happens when technical teams lack financial discipline?",
                "Teknik ekiplerde finansal disiplin olmadığında sıklıkla ne olur?",
                "Expenses accumulate rapidly and the project may fail prematurely",
                ["The software code converts into machine binary automatically", "All developers are promoted to senior executive roles immediately", "The company website receives millions of visits without marketing"],
                "Paragraph 1 explains that without financial discipline, expenses accumulate and projects fail.",
                "1. paragraf finansal disiplin olmadığında harcamaların hızla biriktiğini ve projenin çöktüğünü belirtir."
            ),
            build_q(
                "q_r_a2_06_02",
                "Which expense is considered a direct fixed cost in the article?",
                "Makalede hangi harcama doğrudan sabit maliyet olarak kabul edilir?",
                "Monthly cloud server subscriptions and developer software tools",
                ["Catering for an annual corporate celebration banquet", "New office furniture for visiting foreign dignitaries", "Television advertisements during international sporting events"],
                "Paragraph 2 lists monthly server subscriptions and software tools as direct costs.",
                "2. paragraf aylık sunucu aboneliklerini ve geliştirici araçlarını doğrudan maliyet sayar."
            ),
            build_q(
                "q_r_a2_06_03",
                "Why should project managers include a contingency reserve?",
                "Proje yöneticileri neden bir beklenmedik durum payı eklemelidir?",
                "To absorb unexpected price increases and unforeseen technical hours",
                ["To pay personal holiday bonuses to company executives", "To replace all office desks with gold-plated furniture", "To invest in speculative real estate markets overseas"],
                "Paragraph 3 states that a contingency buffer handles unexpected costs without halting work.",
                "3. paragraf bu payın beklenmedik fiyat artışlarını karşılayıp işin durmasını önlediğini açıklar."
            ),
            build_q(
                "q_r_a2_06_04",
                "What percentage range is recommended for the contingency buffer?",
                "Beklenmedik durum payı için hangi yüzde aralığı önerilmektedir?",
                "Between ten and fifteen percent of the overall budget",
                ["Exactly eighty-five percent of all company revenue", "Less than zero point zero one percent", "Fifty percent every single morning"],
                "Paragraph 3 recommends adding a ten to fifteen percent contingency reserve.",
                "3. paragraf yüzde 10 ila 15 oranında pay eklenmesini önerir."
            ),
            build_q(
                "q_r_a2_06_05",
                "What is a variable expense according to the passage?",
                "Metne göre değişken gider nedir?",
                "An expense that changes depending on workload, such as freelance design help",
                ["A fixed yearly fee charged by the national government", "The price of paper clips purchased five years ago", "The salary of the company's full-time permanent president"],
                "Paragraph 2 defines variable expenses as those that fluctuate, like freelance help.",
                "2. paragraf değişken harcamaların serbest zamanlı tasarımcı gibi iş yüküne göre değiştiğini söyler."
            )
        ],
        "topic_tags": ["budgeting", "project-finance", "cost-management", "a2-reading"],
        "related_ids": ["vocab.contingency", "vocab.capital"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.a2.team-collaboration-tools",
        "title": "Modern Digital Tools for Global Engineering Collaboration",
        "cefr_level": "A2",
        "category": "technology",
        "summary_en": "An examination of how version control repositories, digital issue boards, and chat channels empower distributed technical teams to build software together.",
        "summary_tr": "Sürüm kontrol sistemlerinin, dijital iş panolarının ve sohbet kanallarının dağıtık teknik ekiplerin birlikte yazılım geliştirmesini nasıl sağladığı.",
        "word_count": 300,
        "estimated_reading_minutes": 2,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Distributed Workplace Reality",
                "content_en": "Today, software engineering teams rarely share a single physical office room. Developers in Istanbul frequently collaborate with product managers in London and designers in Berlin. To coordinate complex technical deliverables across multiple time zones, international organizations rely on a suite of modern digital collaboration tools.",
                "content_tr": "Günümüzde yazılım mühendisliği ekipleri nadiren tek bir fiziksel ofis odasını paylaşır. İstanbul'daki yazılımcılar sıklıkla Londra'daki ürün yöneticileri ve Berlin'deki tasarımcılarla iş birliği yapar. Birden fazla saat diliminde karmaşık teknik çıktıları koordine etmek için uluslararası kuruluşlar modern dijital iş birliği araçlarına güvenir."
            },
            {
                "paragraph_index": 2,
                "title": "Version Control and Code Repositories",
                "content_en": "At the foundation of technical collaboration is the version control repository. Platforms like GitHub and GitLab allow hundreds of engineers to work on the exact same codebase simultaneously without overwriting each other's code. Developers submit pull requests, allowing peers to review syntax, detect bugs, and suggest optimizations before code is merged.",
                "content_tr": "Teknik iş birliğinin temelinde sürüm kontrol deposu yer alır. GitHub ve GitLab gibi platformlar yüzlerce mühendisin birbirinin kodunun üzerine yazmadan tam olarak aynı kod tabanında eşzamanlı çalışmasına olanak tanır. Yazılımcılar çekme istekleri (pull requests) göndererek kod birleştirilmeden önce meslektaşlarının sözdizimini incelemesine, hataları bulmasına ve iyileştirmeler önermesine imkan verir."
            },
            {
                "paragraph_index": 3,
                "title": "Transparent Task Management Boards",
                "content_en": "Equally important are digital Kanban boards. Instead of sending endless status emails, teams visualize their progress across columns labeled 'To Do', 'In Progress', and 'Done'. Every ticket clearly displays who is responsible, the target sprint deadline, and relevant technical documentation, ensuring complete organizational transparency.",
                "content_tr": "Aynı derecede önemli olan diğer bir unsur dijital Kanban panolarıdır. Ekipler sonsuz durum e-postaları göndermek yerine ilerlemelerini 'Yapılacak', 'Sürüyor' ve 'Tamamlandı' sütunları üzerinden görselleştirir. Her bilet kimin sorumlu olduğunu, hedef sprint teslim tarihini ve ilgili teknik belgeleri net şekilde göstererek tam bir kurumsal şeffaflık sağlar."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "repository",
                "vocab_id": "vocab.repository",
                "context_definition_en": "A central digital location where code and version histories are stored.",
                "context_meaning_tr": "Kodların ve sürüm geçmişlerinin saklandığı merkezi dijital depo."
            },
            {
                "word": "transparency",
                "vocab_id": "vocab.transparency",
                "context_definition_en": "The quality of being open, clear, and visible to everyone in an organization.",
                "context_meaning_tr": "Bir organizasyonda bilginin herkese açık, net ve görünür olması (şeffaflık)."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_a2_07_01",
                "Why are digital collaboration tools essential for contemporary software squads?",
                "Çağdaş yazılım ekipleri için dijital iş birliği araçları neden vazgeçilmezdir?",
                "Because engineers frequently work distributed across multiple cities and time zones",
                ["Because physical office buildings are legally banned in North America", "Because software cannot run without video cameras continuously active", "Because developers are not permitted to speak out loud"],
                "Paragraph 1 notes that teams work distributed across Istanbul, London, Berlin, etc.",
                "1. paragraf ekiplerin farklı şehir ve saat dilimlerine dağılmış olarak çalıştığını açıklar."
            ),
            build_q(
                "q_r_a2_07_02",
                "How do version control repositories prevent code conflicts?",
                "Sürüm kontrol depoları kod çakışmalarını nasıl önler?",
                "By allowing many engineers to work simultaneously without overwriting files",
                ["By automatically deleting code whenever an engineer makes a mistake", "By limiting total company source code to only twenty lines", "By turning off computer monitors during compile times"],
                "Paragraph 2 states repositories let hundreds of engineers work without overwriting each other.",
                "2. paragraf depoların birbirinin kodunun üzerine yazmadan çalışmayı sağladığını belirtir."
            ),
            build_q(
                "q_r_a2_07_03",
                "What is the function of a 'pull request' mentioned in the text?",
                "Metinde bahsedilen 'pull request' mekanizmasının işlevi nedir?",
                "To let colleagues review, test, and approve code before merging",
                ["To request an immediate cash advance from the finance director", "To demand that all other developers stop working for the day", "To shut down the entire cloud datacenter for maintenance"],
                "Paragraph 2 explains that pull requests let peers review syntax and detect bugs before merging.",
                "2. paragraf birleştirmeden önce meslektaşların kodu incelemesini sağladığını ifade eder."
            ),
            build_q(
                "q_r_a2_07_04",
                "How do digital Kanban boards replace endless status emails?",
                "Dijital Kanban panoları sonsuz durum e-postalarının yerini nasıl alır?",
                "By visualizing tasks across clear columns like 'To Do' and 'Done'",
                ["By automatically printing emails onto paper every evening", "By requiring daily voice phone calls with every customer", "By blocking all incoming internet connections"],
                "Paragraph 3 describes visualizing tasks across columns like To Do, In Progress, and Done.",
                "3. paragraf görevleri sütunlar halinde görselleştirdiğini belirtir."
            ),
            build_q(
                "q_r_a2_07_05",
                "What information is typically shown on a digital task ticket?",
                "Dijital bir görev biletinde genellikle hangi bilgiler gösterilir?",
                "The assigned engineer, target deadline, and technical documentation",
                ["The personal home address of the company CEO", "The current market price of airline tickets to Tokyo", "A list of employee dietary preferences for dinner"],
                "Paragraph 3 lists who is responsible, target deadlines, and relevant documentation.",
                "3. paragraf sorumlu kişiyi, son teslim tarihini ve belgeleri listeler."
            )
        ],
        "topic_tags": ["collaboration-tools", "github", "kanban", "technology", "a2-reading"],
        "related_ids": ["vocab.repository", "vocab.transparency"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.a2.customer-support-excellence",
        "title": "Delivering Exceptional Support to International Clients",
        "cefr_level": "A2",
        "category": "business_strategy",
        "summary_en": "Core principles of outstanding customer service: active listening, empathetic problem resolution, setting realistic expectations, and follow-through.",
        "summary_tr": "Mükemmel müşteri hizmetinin temel ilkeleri: etkin dinleme, empatik problem çözme, gerçekçi beklentiler belirleme ve takip etme.",
        "word_count": 320,
        "estimated_reading_minutes": 2,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Value of Active Listening",
                "content_en": "When a customer contacts your support desk with an urgent issue, they often feel anxious or frustrated. The first rule of great service is active listening. Rather than interrupting the client with generic technical explanations, let them explain their problem fully. Acknowledging their frustration with empathy builds trust and diffuses tension immediately.",
                "content_tr": "Bir müşteri acil bir sorunla destek masanızla iletişime geçtiğinde, genellikle endişeli veya hayal kırıklığına uğramış hisseder. Harika hizmetin ilk kuralı etkin dinlemedir. Müşterinin sözünü genel teknik açıklamalarla kesmek yerine, sorununu tam olarak açıklamasına izin verin. Hayal kırıklıklarını empatiyle kabul etmek güven oluşturur ve gerginliği anında dağıtır."
            },
            {
                "paragraph_index": 2,
                "title": "Honest Timelines Over False Promises",
                "content_en": "When a complex software bug occurs, it is tempting to promise an instant fix to make the customer happy. However, missing an unrealistic deadline damages credibility far more than admitting that an investigation takes time. Clearly explain what steps your engineering team is taking, and give a realistic time window for the next progress update.",
                "content_tr": "Karmaşık bir yazılım hatası ortaya çıktığında, müşteriyi mutlu etmek için anında çözüm sözü vermek cazip gelebilir. Ancak gerçekçi olmayan bir teslim tarihini kaçırmak, araştırmanın zaman alacağını kabul etmekten çok daha fazla güvenilirliğe zarar verir. Mühendislik ekibinizin hangi adımları attığını net bir şekilde açıklayın ve bir sonraki ilerleme güncellemesi için gerçekçi bir zaman aralığı verin."
            },
            {
                "paragraph_index": 3,
                "title": "Proactive Follow-Through",
                "content_en": "Excellent customer support does not end when the ticket is marked resolved. Reaching out twenty-four hours later to confirm that the customer's workflow is running smoothly turns an ordinary interaction into a memorable partnership. This proactive follow-through proves that your organization genuinely cares about the client's long-term success.",
                "content_tr": "Mükemmel müşteri desteği, bilet çözüldü olarak işaretlendiğinde sona ermez. Müşterinin iş akışının sorunsuz çalıştığını doğrulamak için yirmi dört saat sonra iletişime geçmek, sıradan bir etkileşimi unutulmaz bir ortaklığa dönüştürür. Bu proaktif takip, kuruluşunuzun müşterinin uzun vadeli başarısını gerçekten önemsediğini kanıtlar."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "empathy",
                "vocab_id": "vocab.empathy",
                "context_definition_en": "The ability to understand and share the feelings and perspectives of another person.",
                "context_meaning_tr": "Başka birinin duygularını ve bakış açısını anlama ve paylaşma yeteneği (empati)."
            },
            {
                "word": "credibility",
                "vocab_id": "vocab.credibility",
                "context_definition_en": "The quality of being trusted, believable, and reliable.",
                "context_meaning_tr": "Güvenilir, inanılır ve itibar sahibi olma niteliği."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_a2_08_01",
                "What is the first fundamental rule of exceptional customer support?",
                "Olağanüstü müşteri desteğinin ilk temel kuralı nedir?",
                "Listening actively and letting the client explain their issue fully",
                ["Immediately disconnecting the phone call if the client is angry", "Forwarding the customer's email to a competitor's office", "Charging an extra fee before answering any question"],
                "Paragraph 1 states the first rule is active listening without interrupting.",
                "1. paragraf ilk kuralın söz kesmeden etkin dinleme olduğunu belirtir."
            ),
            build_q(
                "q_r_a2_08_02",
                "Why should support agents avoid promising instant fixes for complex bugs?",
                "Destek uzmanları karmaşık hatalar için neden anında çözüm vaat etmekten kaçınmalıdır?",
                "Failing to meet unrealistic promises severely damages customer credibility",
                ["Because software bugs fix themselves automatically after twenty minutes", "Because modern computers cannot be repaired by human beings", "Because instant fixes are forbidden by international commerce law"],
                "Paragraph 2 explains that missing unrealistic deadlines damages credibility.",
                "2. paragraf gerçekçi olmayan vaatleri kaçırmanın güvenilirliği zedelediğini açıklar."
            ),
            build_q(
                "q_r_a2_08_03",
                "What should an agent communicate when an issue takes time to investigate?",
                "Bir sorunun incelenmesi zaman aldığında temsilci neyi iletmelidir?",
                "The concrete steps being taken and a realistic update timeline",
                ["A false claim that the customer caused the problem themselves", "A series of complex binary code formulas without explanation", "A complete refusal to answer future messages"],
                "The text advises clearly explaining steps being taken and giving a realistic time window.",
                "Metin atılan adımları açıklamayı ve gerçekçi bir zaman aralığı vermeyi önerir."
            ),
            build_q(
                "q_r_a2_08_04",
                "What is 'proactive follow-through' as described in the third paragraph?",
                "Üçüncü paragrafta açıklanan 'proaktif takip' nedir?",
                "Contacting the client 24 hours later to ensure everything runs smoothly",
                ["Sending advertising catalogs to the customer every five minutes", "Charging the customer a monthly fee for closing the ticket", "Asking the customer to write the software code themselves"],
                "Paragraph 3 describes reaching out 24 hours later to confirm smooth operation.",
                "3. paragraf her şeyin yolunda gittiğini teyit etmek için 24 saat sonra aramayı açıklar."
            ),
            build_q(
                "q_r_a2_08_05",
                "What long-term benefit does proactive follow-through create?",
                "Proaktif takip uzun vadede ne gibi bir fayda sağlar?",
                "It transforms a routine support call into a trusted business partnership",
                ["It eliminates the need for future customer support staff entirely", "It guarantees that software will never experience another bug", "It makes all company services completely free for everyone"],
                "The text states it turns an ordinary interaction into a memorable partnership.",
                "Metin sıradan bir etkileşimi unutulmaz bir ortaklığa dönüştürdüğünü ifade eder."
            )
        ],
        "topic_tags": ["customer-support", "client-relations", "business-strategy", "a2-reading"],
        "related_ids": ["vocab.empathy", "vocab.credibility"],
        "status": "APPROVED",
        "version": 1
    }
]

print(f"Defined {len(A2_READING_ARTICLES)} A2 reading articles.")
