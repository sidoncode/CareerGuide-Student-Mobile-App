<?php

if (!defined('APP')) {
    die('Access denied');
}

class main extends Controller {

    private $con;
    private $api_key;
    private $api_url;

    public function __construct() {
        $this->con = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
        $this->api_key = 'D7DC21B2-2G71-4CEE-950F-0019675AB74B';
        $this->api_url = 'https://devapi.careerguide.com/api/';
        if ($this->con->connect_error) {
            die("Db Connection failed : " . $this->con->connect_error);
        }
    }

    public function index() {
        try {
            $result = array(
                'status' => true,
                'msg' => 'Welcome CareerGuide.com'
            );
            echo json_encode($result);
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
        }
    }

    public function get_user($key, $val) {
        try {
            $query = "SELECT IF (user_auth IS NULL, '',user_auth) AS user_auth,id,email,password,first_name,last_name,profile_pic,dob,IF (mobile_number IS NULL, '',mobile_number) AS mobile_number,gender,city,activated,IF (education_level IS NULL, '',education_level) AS education_level, IF(status_time<DATE_SUB(NOW(), INTERVAL 6 SECOND),status,'offline') AS online_status FROM users WHERE $key='" . $val . "'";
            if ($user = $this->con->query($query)) {
                if ($user->num_rows > 0) {
                    $row = $user->fetch_assoc();
                    if (!empty($row['profile_pic'])) {
                        $row['profile_pic'] = WEB_MEDIA . DS . $row['profile_pic'];
                    }
                    $result = array(
                        'status' => true,
                        'row' => $row,
                        'msg' => 'user already exist'
                    );
                    return $result;
                } else {
                    throw new Exception("user does not exist");
                }
            } else {
                throw new Exception("db query failed : ".$this->con->error);
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function user_exist() {
        try {
            if (isset($_POST['email']) && $_POST['password']) {
                $email = $_POST['email'];
                $query = "SELECT IF (user_auth IS NULL, '',user_auth) AS user_auth,id,email,password,first_name,last_name,profile_pic,dob,IF (mobile_number IS NULL, '',mobile_number) AS mobile_number,gender,city,activated,IF (education_level IS NULL, '',education_level) AS education_level FROM users  WHERE email='" . $email . "'";
                if ($exist = $this->con->query($query)) {
                    if ($exist->num_rows > 0) {
                        $user = $exist->fetch_assoc();
                        if (!empty($user['password'])) {
                            if (sha1($_POST['password']) == $user['password']) {
                                unset($user['password']);
                                $result = array(
                                    'status' => false,
                                    'msg' => 'user exist',
                                    'user' => $user
                                );
                                echo json_encode($result);
                            } else {
                                $result = array(
                                    'status' => true,
                                    'msg' => 'user already exist',
                                );
                                echo json_encode($result);
                            }
//                            die();
                        } else {
                            if (!empty($user['google_id']) && isset($user['google_id'])) {
                                throw new Exception("registered with google");
                            } elseif (!empty($user['facebook_id']) && isset($user['facebook_id'])) {
                                throw new Exception("registered with facebook");
                            } else {
                                throw new Exception("User not found");
                            }
                        }
                    } else {
                        throw new Exception("User not found");
                    }
                } else {
                    throw new Exception("db query failed");
                }
            } else {
                throw new Exception("email field is missing");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function check_social_login($email) {
        try {
            $query = "SELECT * FROM users WHERE email='" . $email . "'";
            if ($exist = $this->con->query($query)) {
                if ($exist->num_rows > 0) {
                    $user = $exist->fetch_assoc();
                    if (!empty($user['google_id'])) {
                        $result = array(
                            'status' => true,
                            'msg' => 'User registered with google',
                        );
                        return $result;
                    } elseif (!empty($user['facebook_id'])) {
                        $result = array(
                            'status' => true,
                            'msg' => 'User registered with facebook',
                        );
                        return $result;
                    } else {
                        throw new Exception("not registerd with sociel account");
                    }
                } else {
                    throw new Exception("User not found");
                }
            } else {
                throw new Exception("db query failed");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            return $result;
        }
    }

    public function social_login() {
        try {
            if (isset($_POST['email'])) {
                $value = '';
                $email = $_POST['email'];
                $query = "SELECT IF (user_auth IS NULL, '',user_auth) AS user_auth,id,email,password,first_name,last_name,IF(profile_pic!='',CONCAT('" . WEB_MEDIA . DS . "',profile_pic),'') AS profile_pic,dob,IF (mobile_number IS NULL, '',mobile_number) AS mobile_number,facebook_id,google_id,gender,city,activated,IF (education_level IS NULL, '',education_level) AS education_level FROM users WHERE email='" . $email . "'";
                if ($exist = $this->con->query($query)) {
                    if ($exist->num_rows > 0) {
                        $user = $exist->fetch_assoc();
                        if (isset($_POST['facebook_id'])) {
                            if ($user['facebook_id'] == '' || empty($user['facebook_id'])) {
                                $que = "UPDATE users SET facebook_id='" . $_POST['facebook_id'] . "' WHERE email='" . $email . "'";
                                if ($update = $this->con->query($que)) {
                                    $new = $this->con->query($query);
                                    $result = array(
                                        'status' => true,
                                        'msg' => 'updated',
                                    );
//                                    echo 'call';
                                } else {
                                    $result = array(
                                        'status' => true,
                                        'msg' => 'user_exist',
                                    );
                                }
                            } else {
                                $result = array(
                                    'status' => true,
                                    'msg' => 'user_exist',
                                );
                            }
                        } elseif (isset($_POST['google_id'])) {
                            if ($user['google_id'] == '' || empty($user['google_id'])) {
                                $que = "UPDATE users SET google_id='" . $_POST['google_id'] . "' WHERE email='" . $email . "'";
                                if ($update = $this->con->query($que)) {
                                    $new = $this->con->query($query);
                                    $result = array(
                                        'status' => true,
                                        'msg' => 'updated',
                                    );
                                } else {
                                    $result = array(
                                        'status' => true,
                                        'msg' => 'user_exist',
                                    );
                                }
                            } else {
                                $result = array(
                                    'status' => true,
                                    'msg' => 'user_exist',
                                );
                            }
                        } else {
                            $result = array(
                                'status' => true,
                                'msg' => 'user_exist',
                            );
                        }
                        unset($user['password']);
                        unset($user['facebook_id']);
                        unset($user['google_id']);
                        $result['data'] = $user;
                        echo json_encode($result);
                    } else {
                        throw new Exception("User not found");
                    }
                } else {
                    throw new Exception("db query failed");
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
        }
    }

    public function user_exist_fun($email) {
        try {
            $query = "SELECT * FROM users WHERE email='" . $email . "'";
            if ($exist = $this->con->query($query)) {
                if ($exist->num_rows > 0) {
                    $result = array(
                        'status' => true,
                        'msg' => 'user_exist',
                        'data' => $exist->fetch_assoc()
                    );
                    return $result;
                } else {
                    throw new Exception("User not found");
                }
            } else {
                throw new Exception("db query failed");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            return $result;
        }
    }

    public function user_exist_fun_id($id) {
        try {
            $query = "SELECT * FROM users WHERE id='" . $id . "'";
            if ($exist = $this->con->query($query)) {
                if ($exist->num_rows > 0) {
                    $result = array(
                        'status' => true,
                        'msg' => 'user_exist',
                        'data' => $exist->fetch_assoc()
                    );
                    return $result;
                } else {
                    throw new Exception("User not found");
                }
            } else {
                throw new Exception("db query failed");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            return $result;
        }
    }

    public function register() {
        try {
            // [email],[first_name],[last_name],[dob],[gender],[city]
            if (filter_var($_POST['email'], FILTER_VALIDATE_EMAIL)) {
                $user_exist = self::user_exist_fun($_POST['email']);
                $facebook_id = $google_id = $profile_pic = $password = NULL;
                $activated = 0;
                if (isset($_POST['password']) && $_POST['password'] != '') {
                    $activated = 0;
                    $password = sha1($_POST['password']);
                }
                if (isset($_POST['facebook_id']) && $_POST['facebook_id'] != '') {
                    $activated = 1;
                    $facebook_id = $_POST['facebook_id'];
                }
                if (isset($_POST['google_id']) && $_POST['google_id'] != '') {
                    $activated = 1;
                    $google_id = $_POST['google_id'];
                }
                if (!isset($_POST['first_name'])) {
                    throw new Exception('First name is missing');
                    die();
                }
                if (!isset($_POST['last_name'])) {
                    throw new Exception('Last name is missing');
                    die();
                }
                if (!isset($_POST['dob'])) {
                    throw new Exception('Date of birth is missing');
                    die();
                }
                if (!isset($_POST['gender'])) {
                    throw new Exception('Gender is missing');
                    die();
                }
                if (!isset($_POST['city'])) {
                    throw new Exception('City is missing');
                    die();
                }
                if (isset($_POST['profile_pic'])) {
                    $profile_pic = self::upload_pic($_POST['profile_pic']);
                    if (empty($profile_pic)) {
                        throw new Exception("Profile pic upload failed");
                        die();
                    } else {
                        if (!empty($user_exist['data']['profile_pic'])) {
                            unlink(MEDIA_DIR . DS . $user_exist['data']['profile_pic']);
                        }
                    }
                } else {
                    $profile_pic = '';
                }
                $email = $_POST['email'];
                $first_name = $_POST['first_name'];
                $last_name = $_POST['last_name'];
                $dob = $_POST['dob'];
                $gender = $_POST['gender'];
                $city = $_POST['city'];
                if ($user_exist['status'] === true) {
                    if (isset($_POST['password']) && $_POST['password'] != '' && $_POST['password'] != NULL) {
                        $stmt = $this->con->prepare("UPDATE users SET first_name=?, password=? ,last_name=?, profile_pic=?, dob=?, gender=?, city=?  WHERE email=?");
                        $stmt->bind_param("ssssssss", $first_name, $password, $last_name, $profile_pic, $dob, $gender, $city, $email);
                    } else if (isset($_POST['facebook_id']) && $_POST['facebook_id'] != '') {
                        $stmt = $this->con->prepare("UPDATE users SET first_name=?, last_name=?, profile_pic=?, dob=?, gender=?, city=?, facebook_id=?, activated=?  WHERE email=?");
                        $stmt->bind_param("sssssssis", $first_name, $last_name, $profile_pic, $dob, $gender, $city, $facebook_id, $activated, $email);
                    } else if (isset($_POST['google_id']) && $_POST['google_id'] != '') {
                        $stmt = $this->con->prepare("UPDATE users SET first_name=?,last_name=?, profile_pic=?, dob=?, gender=?, city=?, google_id=?, activated=?  WHERE email=?");
                        $stmt->bind_param("sssssssis", $first_name, $last_name, $profile_pic, $dob, $gender, $city, $google_id, $activated, $email);
                    } else {
                        $stmt = $this->con->prepare("UPDATE users SET first_name=?, last_name=?, profile_pic=?, dob=?, gender=?, city=?  WHERE email=?");
                        $stmt->bind_param("sssssss", $first_name, $last_name, $profile_pic, $dob, $gender, $city, $email);
                    }
                    if ($stmt->execute()) {
                        $user = self::get_user('email', $_POST['email']);
                        unset($user['row']['password']);

                        $result = array(
                            'status' => true,
                            'user' => $user['row'],
                            'msg' => "Updation Successful",
                        );
                        $subject = $user['row']['first_name'] . ", here is your CareerGuide sign-up bonus!";
                        $msg = "<h3>Hello " . $user['row']['first_name'] . "!</h3>";
                        $msg .= "<p>Welcome on-board. CareerGuide is your one-stop solution for all career related queries from counselling and psychometric tests to admission guidance and study abroad services.</p><p>Our 1100+ career experts help over 5 million students every year in finding their ideal career choice.</p><p>As a sign-up bonus, we are giving you 10 minutes of free counselling session with an expert career counsellor! You can use these Counselling Minutes to ask your questions about college admissions, competitive exams or ask tips about how to excel in a particular chosen career.</p><p>To schedule your free call with our expert Career Counsellor, click <a href='#''>here</a>.</p>
                            <p>
                            Thanks,<br>
                            CareerGuide Admin.
                            </p>";
                        $responce = self::send_mail_fun($user['row']['first_name'], $user['row']['email'], $subject, $msg);
                        echo json_encode($result);
                        die();
                    } else {
                        throw new Exception(filter_var($this->con->error, FILTER_SANITIZE_STRING));
                    }
                    $stmt->close();
                } else {
                    $stmt = $this->con->prepare("INSERT INTO users( email, password, first_name, last_name, profile_pic, dob, gender, city, facebook_id, google_id, activated) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                    $stmt->bind_param("ssssssssssi", $email, $password, $first_name, $last_name, $profile_pic, $dob, $gender, $city, $facebook_id, $google_id, $activated);
                    if ($stmt->execute()) {
                        $user = self::get_user('email', $_POST['email']);
                        $query = "INSERT INTO temp_otp (otp, user_id,email) VALUES(1,'" . $user['row']['id'] . "','" . sha1($user['row']['email']) . "')";
                        $hash = sha1($user['row']['email']);
                        $otp_exist = self::otp_exist_fun($hash);
                        if ($otp_exist['status'] != true) {
                            $this->con->query($query);
                        }
                        $subject = "Welcome to CareerGuide!";
                        $msg = "<h3>Hi " . $user['row']['first_name'] . ",</h3>";
                        $msg .= "<p>Thanks for registering with CareerGuide! CareerGuide is your one-stop solution for all career related queries from counselling and psychometric tests to admission guidance and study abroad services.</p><p>Our 1100+ career experts help over 5 million students every year in finding their ideal career choice.</p><p>As a sign-up bonus, we are happy to provide you 10 minutes of free counselling session with an expert career counsellor. But firstly we need you to verify your account to ensure a personalised and complete experience of the app.</p><p>Click <a href='http://app.careerguide.com/api/main/activate/" . sha1($user['row']['email']) . "/1' > here </a>to activate your account. You could also copy paste​​ the following link in a browser window: http://app.careerguide.com/api/main/activate/" . sha1($user['row']['email']) . "/1</p>
                            <p>
                            Thanks,<br>
                            CareerGuide Admin.
                            </p>";

                        $responce = self::send_mail_fun($user['row']['first_name'], $user['row']['email'], $subject, $msg);
                        unset($user['row']['password']);
                        $result = array(
                            'status' => true,
                            'user' => $user['row'],
                            'msg' => "Registration Successful",
                                //"user_id" => $stmt->insert_id
                        );
                        echo json_encode($result);
                        die();
                    } else {
                        throw new Exception(filter_var($this->con->error, FILTER_SANITIZE_STRING));
                    }
                    $stmt->close();
                }
                //echo $query;
            } else {
                throw new Exception("Please enter a valid email address");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
        }
    }

    public function login() {
        try {
            if (isset($_POST['email']) && isset($_POST['password'])) {
                $user = self::get_user('email', $_POST['email']);
                if ($user['status'] == true) {
                    if ($user['row']['password'] == sha1($_POST['password'])) {
                        unset($user['row']['password']);
                        $result = array(
                            'status' => true,
                            'msg' => "login successful",
                            "user" => $user['row']
                        );
                        echo json_encode($result);
                        die();
                    } else {
                        throw new Exception("Incorrect password");
                    }
                } else {
                    throw new Exception("user does not exist");
                }
            } else {
                throw new Exception("missing require field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
        }
    }

    public function otp_exist_fun($email) {
        try {
            $query = "SELECT email FROM temp_otp WHERE email='" . $email . "'";
            if (!($stmt = $this->con->query($query))) {
                throw new Exception("Prepare statement failed (opt_exist_fun) " . htmlspecialchars($this->con->error));
                die();
            }
            if ($stmt->num_rows > 0) {
                $result = array(
                    'status' => true,
                    'msg' => "Otp already set"
                );
                return $result;
            } else {
                throw new Exception("Otp not available");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            return $result;
        }
    }

    public function forget_password() {
        try {
            if (isset($_POST['email']) && isset($_POST['otp'])) {
                $user = self::get_user('email', $_POST['email']);
                if ($user['status'] == true) {
                    if (!empty($user['row']['password'])) {
                        $query = "INSERT INTO temp_otp (otp, user_id,email) VALUES(?,?,?)";
                        $hash = sha1($_POST['email']);
                        $otp_exist = self::otp_exist_fun($hash);
                        if ($otp_exist['status'] == true) {
                            $query = "UPDATE temp_otp SET otp = ?, user_id = ? WHERE email = ? ";
                        }
                        if (!($stmt = $this->con->prepare($query))) {
                            throw new Exception("Prepare statement failed (forget_password)");
                            die();
                        }
                        if (!$stmt->bind_param("sis", $_POST['otp'], $user['row']['id'], $hash)) {
                            throw new Exception("Query binding failed (forget_password)");
                            die();
                        }
                        if (!$stmt->execute()) {
                            throw new Exception("Query Exceution failed (forget_password)");
                            die();
                        }
                        $stmt->close();
                        $subject = "Reset Password Link from CareerGuide";
                        $msg = "<h3>Hi " . $user['row']['first_name'] . "</h3>";
                        $msg .= "<p>The OTP to reset your password is <strong>" . $_POST['otp'] . "</strong>. You can enter this OTP on the app or click <a href='http://app.careerguide.com/api/main/forgot_otp/" . $hash . "/" . $_POST['otp'] . "'> here </a> to reset your password.</p><p> Alternately, you can also copy paste the following link in a browser to set a new password: http://app.careerguide.com/api/main/forgot_otp/" . $hash . "/" . $_POST['otp'] . " </p>";
                        $msg .= "<p>Thanks,<br>CareerGuide Admin.</p>";
                        $msg .= "<p>PS: Haven't requested for a change in password? Don't worry, just ignore this email and nothing will happen.</p>";
                        $responce = self::send_mail_fun($user['row']['first_name'], $user['row']['email'], $subject, $msg);
                        $social_login = self::check_social_login($_POST['email']);
                        $result = array(
                            'status' => true,
                            'social' => $social_login['status'],
                            'msg' => "otp send successfully"
                        );
                        echo json_encode($result);
                        die();
                    } else {
                        throw new Exception("user not registered with custom");
                    }
                } else {
                    throw new Exception("user does not exist");
                }
            } else {
                throw new Exception("missing require field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function change_password() {
        try {
            if (isset($_POST['email']) && isset($_POST['password'])) {
                $query = "UPDATE users SET password='" . sha1($_POST['password']) . "' WHERE `email`='" . $_POST['email'] . "'";
                if ($this->con->query($query)) {
                    $user = self::get_user('email', $_POST['email']);
                    unset($user['row']['password']);
                    $result = array(
                        'status' => true,
                        'msg' => "password updated successfully",
                        'user_detail' => $user['row']
                    );
                    echo json_encode($result);
                    die();
                } else {
                    throw new Exception("Something went wrong " . $this->con->error);
                }
            }
            else {
              throw new Exception("MIssing required fields");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function upload_pic($profile_pic) {
        $image_name = uniqid() . time() . '.jpeg';
        $encode = file_put_contents(MEDIA_DIR . DS . $image_name, base64_decode($profile_pic));
        if ($encode == true) {
            return $image_name;
        } else {
            return false;
        }
    }

    public function creat_new_user($user_data) {
        $url = $this->api_url . 'create_new_user';
        $ch = curl_init();
        $fields = json_encode(array(
            'api_key' => $this->api_key,
            'first_name' => $user_data['first_name'],
            'last_name' => $user_data['last_name'],
            'email' => $user_data['email'],
            'password' => $user_data['password'],
            'education_level' => $user_data['education_level'],
            'mobile_number' => $user_data['mobile_number']
        ));
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers = array('Content-Type: application/json'));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
        $result = curl_exec($ch);
        $result = $result;
        //echo $fields;
        //echo json_encode($result);
        return $result;
    }

    public function send_mail_fun($name, $email, $subject, $msg) {
        require("/usr/local/bin/mail/sendgrid-php/sendgrid-php.php");
        $from = new SendGrid\Email("Admin", "admin@careerguide.com");
        $to = new SendGrid\Email($name, $email);
        $content = new SendGrid\Content("text/html", $msg);
        $mail = new SendGrid\Mail($from, $subject, $to, $content);
        $apiKey = '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';
        $sg = new \SendGrid($apiKey);
        $response = $sg->client->mail()->send()->post($mail);
        return $response;
    }

    public function contact_us() {
        try {
            if (isset($_POST['name']) && isset($_POST['mobile_number']) && isset($_POST['msg'])) {
                require("/usr/local/bin/mail/sendgrid-php/sendgrid-php.php");
                $from = new SendGrid\Email("Admin", "admin@careerguide.com");
                $subject = $_POST['name'] . " sent you a message via CareerGuide App";
                $to = new SendGrid\Email("Customer Care", "customercare@careerguide.com");
                $html = "<h4>Name : " . $_POST['name'] . "</h4>";
                $html .= "<h4>Mobile No. : " . $_POST['mobile_number'] . "</h4>";
                $html .= "<h4>Message : </h4>";
                $html .= "<p>" . $_POST['msg'] . "</p>";
                $content = new SendGrid\Content("text/html", $html);
                $mail = new SendGrid\Mail($from, $subject, $to, $content);
                $apiKey = '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';
                $sg = new \SendGrid($apiKey);
                $response = $sg->client->mail()->send()->post($mail);
                $result = array(
                    'status' => true,
                    'msg' => 'Mail sent..'
                );
                echo json_encode($result);
                die();
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function forgot_otp($otp) {
        $query = "SELECT * FROM temp_otp WHERE email='" . $otp[0] . "' AND otp='" . $otp[1] . "'";
        if ($stmt = $this->con->query($query)) {
            if ($stmt->num_rows > 0) {
                $otp_temp = $stmt->fetch_assoc();
                if (isset($_POST['password'])) {
                    $status = false;
//                    $query = "SELECT * FROM temp_otp WHERE email='" . $otp[0] . "' AND otp='" . $otp[1] . "'";
                    $query = "UPDATE users SET password='" . sha1($_POST['password']) . "' WHERE id='" . $otp_temp['user_id'] . "'";
//                    echo $query;
                    if ($update = $this->con->query($query)) {
                        if ($update) {
                            $query = "DELETE FROM temp_otp WHERE user_id='" . $otp_temp['user_id'] . "'";
                            $this->con->query($query);
                            $status = true;
                        }
                    }
                    require_once 'recover-pass.php';
                } else {
                    require_once 'recover-pass.php';
                }
            } else {
                echo '<center><h1>This link is Expired ..</h1></center>';
            }
        }
    }

    public function user_activation_status() {
        try {
            if (isset($_POST['user_id'])) {
                $query = "SELECT IF (user_auth IS NULL, '',user_auth) AS user_auth, activated, IF (mobile_number IS NULL, '',mobile_number) AS mobile_number,IF (education_level IS NULL, '',education_level) AS education_level  FROM users WHERE `id`='" . $_POST['user_id'] . "' ";
                $user = self::get_user('id', $_POST['user_id']);
                if ($act = $this->con->query($query)) {
                    if ($act->num_rows > 0) {
                        $data = $act->fetch_assoc();

                        $result = array(
                            'status' => true,
                            'activated' => $data['activated'],
                            'mobile_number' => $data['mobile_number'],
                            'education_level' => $data['education_level'],
                            'user_auth' => $data['user_auth']
                        );
                        if ($data['activated'] == 0) {
                            $query = "INSERT INTO temp_otp (otp, user_id,email) VALUES(1,'" . $user['row']['id'] . "','" . sha1($user['row']['email']) . "')";
                            $hash = sha1($user['row']['email']);
                            $otp_exist = self::otp_exist_fun($hash);
                            if ($otp_exist['status'] != true) {
                                $this->con->query($query);
                            }
                            $subject = "Verify your account ( careerguide.com )";
                            $msg = "<h3>Hi " . $user['row']['first_name'] . "</h3>";
                            $msg .= "<p> Your account is not verifiyed yet! <strong> Click on the link to verify</strong> <br>Click here to activate <a href='http://app.careerguide.com/api/main/activate/" . sha1($user['row']['email']) . "/1' > Reset Your password </a> Or http://app.careerguide.com/api/main/activate/" . sha1($user['row']['email']) . "/1' for verfying </p>";
                            $responce = self::send_mail_fun($user['row']['first_name'], $user['row']['email'], $subject, $msg);
                        }
                        if ($data['mobile_number'] == '' || empty($data['mobile_number'])) {
                            $flags = self::flag_cal();
                            $result['flags'] = $flags['data'];
                        }
                        echo json_encode($result);
                        die();
                    } else {
                        throw new Exception("User Not found ");
                    }
                } else {
                    throw new Exception("Something went wrong " . $this->con->error);
                }
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function update_mobile() {
        try {
            if (isset($_POST['user_id']) && isset($_POST['mobile'])) {
                $query = "UPDATE users SET mobile_number='" . $_POST['mobile'] . "' WHERE `id`='" . $_POST['user_id'] . "'";
                if ($this->con->query($query)) {
                    $user = self::get_user('id', $_POST['user_id']);
                    unset($user['row']['password']);
                    $result = array(
                        'status' => true,
                        'msg' => "mobile number updated successfully",
                        'user_detail' => $user['row']
                    );
                    echo json_encode($result);
                    die();
                } else {
                    throw new Exception("Something went wrong " . $this->con->error);
                }
            }
            else {
              throw new Exception("Missing required fields");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function update_education_level() {
        try {
            if (isset($_POST['user_id']) && isset($_POST['education_level'])) {

                $query = "UPDATE users SET education_level='" . $_POST['education_level'] . "' WHERE `id`='" . $_POST['user_id'] . "'";
                if ($this->con->query($query)) {
                    $user = self::get_user('id', $_POST['user_id']);
                    unset($user['row']['password']);
                    $result = array(
                        'status' => true,
                        'msg' => "education level updated",
                        'user_detail' => $user['row']
                    );
                    echo json_encode($result);
                    die();
                } else {
                    throw new Exception("Something went wrong " . $this->con->error);
                }
            }
            else{
              throw new Exception("Missing required fields");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function creat_auth_key() {
        try {
            if (isset($_POST['user_id'])) {
                $user = self::get_user('id', $_POST['user_id']);
                $creat_new = self::creat_new_user($user['row']);
//                print_r($creat_new);die();
                $creat = json_decode($creat_new);
                if (isset($creat[0]->status) && $creat[0]->status == 'success') {
                    $query = "UPDATE users SET user_auth='" . $creat[0]->user_auth . "' WHERE id='" . $_POST['user_id'] . "'";
                    if ($this->con->query($query)) {
                        unset($user['row']['password']);
                        $result = array(
                            'status' => true,
                            'msg' => "User created successfully",
                            'user_auth' => $creat[0]->user_auth
                        );
                        echo json_encode($result);
                    } else {
                        throw new Exception("Something went wrong " . $this->con->error);
                    }
                } else {
                    throw new Exception("Something went wrong error from server" . $creat);
                }
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function activate($otp) {
        $query = "SELECT * FROM temp_otp WHERE email='" . $otp[0] . "' AND otp='" . $otp[1] . "'";
        if ($stmt = $this->con->query($query)) {
            if ($stmt->num_rows > 0) {
                $otp_temp = $stmt->fetch_assoc();
                $status = false;
//                    $query = "SELECT * FROM temp_otp WHERE email='" . $otp[0] . "' AND otp='" . $otp[1] . "'";
                $query = "UPDATE users SET activated='1' WHERE id='" . $otp_temp['user_id'] . "'";
//                    echo $query;
                if ($update = $this->con->query($query)) {
                    if ($update) {
                        $query = "DELETE FROM temp_otp WHERE user_id='" . $otp_temp['user_id'] . "'";
                        self::sentmailafteractivation($otp_temp['user_id']);
                        $this->con->query($query);
                        echo '<h1><center>Account verified successfully</center></h1>';
                    }
                }
            } else {
                echo '<center><h1>This link is Expired ..</h1></center>';
            }
        }
    }

    public function sentmailafteractivation($user_id) {
        $user = self::get_user('id', $user_id);
        $subject = $user['row']['first_name'] . ", here is your sign-up bonus from CareerGuide!";
        $msg = "<h3>Hi " . $user['row']['first_name'] . "!</h3>";
        $msg .= "<p>Here is your confirmation email for availing 10 minutes of free counselling session with our career expert. You can use these Counselling Minutes to ask your questions about college admissions, competitive exams or take tips about how to excel in a particular choosen career.</p><p>To schedule your free call with our expert Career Counsellor, click <a href='#' > Verify your account </a> (app_open: Schedule Call). </p><p>Thanks,<br>CareerGuide Team.</p>";
        return $responce = self::send_mail_fun($user['row']['first_name'], $user['row']['email'], $subject, $msg);
    }

    public function admin_acknowledge() {
        try {
            if (isset($_POST['desc']) && isset($_POST['name']) && isset($_POST['mobile']) && isset($_POST['uc'])) {
                $subject = "Consellor registeration request repeat";
                $msg = "<h3>Hello !</h3>";
                $msg .= "<p>Counsellor registration request repeated.</p>
                                    <p>
                                    Name : " . $_POST['name'] . "<br>
                                    Mobile : " . $_POST['mobile'] . "<br>
                                    Description : " . $_POST['desc'] . "<br>
                                    UC : " . $_POST['uc'] . "<br><br>";
                $msg .= "</p>
                                        <p>
                                        Thanks,<br>
                                        CareerGuide Admin.
                                        </p>";
                $result = array(
                    'status' => true,
                    'msg' => 'Mail sent'
                );
                echo json_encode($result);
                $responce = self::send_mail_fun('Admin', 'surabhi@careerguide.com', $subject, $msg);
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function counsellor_registration() {
        try {
            $number = '';
            if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['mobile']) && isset($_POST['linkedin_profile_link'])) {
                $desc = (isset($_POST['desc'])) ? $_POST['desc'] : '';
                $stmt2 = $this->con->query("SELECT * FROM counsellor WHERE email='" . $_POST['email'] . "'");
                $desc = (isset($_POST['desc'])) ? $_POST['desc'] : '';
                if ($stmt2->num_rows > 0) {
                    $coun = $stmt2->fetch_assoc();
                    $desc = $coun['desc'];
                    $number = $coun['uc'];
                    $result = array(
                        'status' => true,
                        'msg' => 'Counsellor already registered'
                    );
                    $subject = "Consellor registeration request repeat";
                    $msg = "<h3>Hello !</h3>";
                    $msg .= "<p>Counsellor registration request repeated.</p>
                                <p>
                                Email : " . $_POST['email'] . "<br>
                                Name : " . $_POST['name'] . "<br>
                                Mobile : " . $_POST['mobile'] . "<br>
                                Linkdin profile link : " . $_POST['linkedin_profile_link'] . "<br>
                                Description : " . $desc . "<br>
                                UC : " . $number . "<br><br>";
                    if (isset($_POST['user_id'])) {
                        $user = self::get_user('id', $_POST['user_id']);
                        $msg .= "<b>Registered User details</b><br><br>";
                        foreach ($user['row'] as $key => $value) {
                            if ($key == 'user_auth' || $key == "password") {
                                continue;
                            } else if ($key == 'id') {
                                $key = 'Registered User id';
                            } else if ($key == 'email') {
                                $key = 'Registered email';
                            } else if ($key == 'activated') {
                                $value = $value == 1 ? 'True' : 'False';
                            }
                            $key = ucwords($key);
                            $key = str_replace("_", " ", $key);
                            $msg .= $key . " : " . $value . "<br>";
                        }
                    }
                    $msg .= "</p>
                                    <p>
                                    Thanks,<br>
                                    CareerGuide Admin.
                                    </p>";
                    echo json_encode($result);
                    $responce = self::send_mail_fun('Admin', 'surabhi@careerguide.com', $subject, $msg);
                } else {

                    if (isset($_POST['user_id'])) {
                        $number = self::ucg();
                        $user_id = $_POST['user_id'];
                        $uac_available = 'NULL';
                        $is_counsellor = 'NULL';
                        $subject = "New consellor register";
                        $msg = "<h3>Hello !</h3>";
                        $msg .= "<p>A new counsellor registration request recieved.</p>
                                <p>
                                Email : " . $_POST['email'] . "<br>
                                Name : " . $_POST['name'] . "<br>
                                Mobile : " . $_POST['mobile'] . "<br>
                                Linkdin profile link : " . $_POST['linkedin_profile_link'] . "<br>
                                Description : " . $desc . "<br>
                                UC : " . $number . "<br><br>";
                        $user = self::get_user('id', $_POST['user_id']);
                        $msg .= "<b>Registered User details</b><br><br>";
                        foreach ($user['row'] as $key => $value) {
                            if ($key == 'user_auth' || $key == "password") {
                                continue;
                            } else if ($key == 'id') {
                                $key = 'Registered User id';
                            } else if ($key == 'email') {
                                $key = 'Registered email';
                            } else if ($key == 'activated') {
                                $value = $value == 1 ? 'True' : 'False';
                            }
                            $key = ucwords($key);
                            $key = str_replace("_", " ", $key);
                            $msg .= $key . " : " . $value . "<br>";
                        }
                        $msg .= "</p>
                                    <p>
                                    Thanks,<br>
                                    CareerGuide Admin.
                                    </p>";
                        $responce = self::send_mail_fun('Admin', 'surabhi@careerguide.com', $subject, $msg);
                        if ($stmt = $this->con->prepare("INSERT INTO counsellor (`user_id`,`email`,`name`,`mobile`,`linkdin_profile`,`desc`,`uc`) VALUES(?,?,?,?,?,?,?)")) {
                            if ($stmt->bind_param('ississs', $user_id, $_POST['email'], $_POST['name'], $_POST['mobile'], $_POST['linkedin_profile_link'], $desc, $number)) {
                                if ($stmt->execute()) {
                                    $result = array(
                                        'status' => true,
                                        'msg' => 'Counsellor successful register'
                                    );
                                    echo json_encode($result);
                                } else {
                                    throw new Exception("Query execution failed");
                                }
                            } else {
                                throw new Exception('Binding parametrs failed');
                            }
                        } else {
                            throw new Exception('Prepare statement failed');
                        }
                    } else {
                        $subject = "New consellor register";
                        $msg = "<h3>Hello !</h3>";
                        $msg .= "<p>A new counsellor registration request recieved.</p>
                                <p>
                                Email : " . $_POST['email'] . "<br>
                                Name : " . $_POST['name'] . "<br>
                                Mobile : " . $_POST['mobile'] . "<br>
                                Linkdin profile link : " . $_POST['linkedin_profile_link'] . "<br>
                                Description : " . $desc . "<br>";
                        $msg .= "UAC Available : " . $is_avalibale = ($_POST['uac_available'] == '1') ? 'True' : 'False';
                        $msg .= '<br>';
                        $msg .= "Is a Counsellor : " . $is_avalibale = ($_POST['is_counsellor'] == '1') ? 'True' : 'False';
                        $msg .= "</p>
                                    <p>
                                    Thanks,<br>
                                    CareerGuide Admin.
                                    </p>";
                        $responce = self::send_mail_fun('Admin', 'surabhi@careerguide.com', $subject, $msg);
                        $result = array(
                            'status' => true,
                            'msg' => 'Mail sent'
                        );
                        echo json_encode($result);
                    }
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function ucg() {
        $number = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        $number = str_shuffle($number);
        $number = substr($number, 1, 6);
        $query = "SELECT * FROM counsellor WHERE uc='" . $number . "'";
        $exist = $this->con->query($query);
        if ($exist->num_rows > 0) {
            $number = $this->usg();
        }
        return $number;
    }

    public function check_uc() {
        try {
            if (isset($_POST['password']) && isset($_POST['uc']) && isset($_POST['email'])) {
                $stmt = $this->con->query("SELECT * FROM counsellor JOIN users WHERE uc='" . $_POST['uc'] . "' AND counsellor.email='" . $_POST['email'] . "'");
                if ($stmt->num_rows > 0) {
                    $row = $stmt->fetch_assoc();
                    if ($row['password'] != '') {
                        if ($row['password'] != sha1($_POST['password'])) {

                            throw new Exception('Password is incorrect');
                        } else {
                            $result = array(
                                'status' => true,
                                'msg' => "Uc is valid"
                            );
                            echo json_encode($result);
                        }
                    } else {
                        throw new Exception('Password not set yet');
                    }
                } else {
                    throw new Exception('Email or uc is invalid');
                }
                echo json_encode($result);
            } elseif (isset($_POST['uc']) && isset($_POST['email'])) {
                $stmt = $this->con->query("SELECT * FROM counsellor WHERE uc='" . $_POST['uc'] . "' AND email='" . $_POST['email'] . "' ");
                if ($stmt->num_rows > 0) {
                    $result = array(
                        'status' => true,
                        'msg' => "Uc is valid"
                    );
                    echo json_encode($result);
                } else {
                    throw new Exception('Uc is not valid');
                }
            } elseif (isset($_POST['uc'])) {
                $stmt = $this->con->query("SELECT * FROM counsellor WHERE uc='" . $_POST['uc'] . "'");
                if ($stmt->num_rows > 0) {
                    $result = array(
                        'status' => true,
                        'msg' => "Uc is valid"
                    );
                    echo json_encode($result);
                } else {
                    throw new Exception('Uc is invalid');
                }
            } else {
                throw new Exception('Missing required field');
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function noti_pre() {
        try {
            if (isset($_POST['email']) && isset($_POST['message']) && isset($_POST['call']) && isset($_POST['user_id'])) {
                $base_query = "INSERT INTO noti_prefer (`email`,`message`,`call`,`user_id`) VALUES(?,?,?,?)";
                if ($stm = $this->con->prepare("SELECT * FROM noti_prefer WHERE user_id=?")) {
                    if ($stm->bind_param('i', $_POST['user_id'])) {
                        if ($stm->execute()) {
                            $stm->store_result();
                            if ($stm->num_rows > 0) {
                                $base_query = "UPDATE noti_prefer SET `email`=?,`message`=?,`call`=? WHERE user_id=?";
                            }
                        } else {
                            throw new Exception("Query Execution failed");
                        }
                    } else {
                        throw new Exception("Query binding failed");
                    }
                } else {
                    throw new Exception("Query prepare failed");
                }
                if ($stmt = $this->con->prepare($base_query)) {
                    if ($stmt->bind_param('iiii', $_POST['email'], $_POST['message'], $_POST['call'], $_POST['user_id'])) {
                        if ($stmt->execute()) {
                            $result = array(
                                'status' => true,
                                'msg' => 'notification prefrence saved'
                            );
                            echo json_encode($result);
                        } else {
                            throw new Exception("Query execution failed");
                        }
                    } else {
                        throw new Exception('Binding parametrs failed');
                    }
                } else {
                    throw new Exception('Prepare statement failed');
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function get_noti_pre() {
        try {
            if (isset($_POST['user_id'])) {
                if ($stmt = $this->con->prepare("SELECT * FROM noti_prefer WHERE user_id=?")) {
                    if ($stmt->bind_param('i', $_POST['user_id'])) {
                        if ($stmt->execute()) {
                            $res_data = $stmt->get_result();
                            if ($res_data->num_rows > 0) {
                                $data = $res_data->fetch_assoc();
                                $result = array(
                                    'status' => true,
                                    'noti_pre' => $data
                                );
                                echo json_encode($result);
                            } else {
                                $data = array(
                                    'user_id' => $_POST['user_id'],
                                    'email' => 1,
                                    'message' => 1,
                                    'call' => 1,
                                    'date_time' => date('Y-m-d H:i:s')
                                );
                                $result = array(
                                    'status' => true,
                                    'noti_pre' => $data
                                );
                                echo json_encode($result);
                            }
                        } else {
                            throw new Exception("Query execution failed");
                        }
                    } else {
                        throw new Exception('Binding parametrs failed');
                    }
                } else {
                    throw new Exception('Prepare statement failed');
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function user_register_status() {
        try {
            if (isset($_POST['user_id'])) {
                $query = "SELECT * FROM users WHERE id='" . $_POST['user_id'] . "'";
                if ($exist = $this->con->query($query)) {
                    if ($exist->num_rows > 0) {
                        $user = $exist->fetch_assoc();
                        if (!empty($user['password'])) {
                            $result = array(
                                'status' => true,
                                'msg' => 'User registered with custome password',
                            );
                            echo json_encode($result);
                        } else {
                            throw new Exception("user not registered with custom");
                        }
                    } else {
                        throw new Exception("User not found");
                    }
                } else {
                    throw new Exception("db query failed");
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function check_password() {
        try {
            if (isset($_POST['user_id']) && $_POST['password']) {
                if ($stmt = $this->con->prepare("SELECT * FROM users WHERE id=?")) {
                    if ($stmt->bind_param('i', $_POST['user_id'])) {
                        if ($stmt->execute()) {
                            $res_data = $stmt->get_result();
                            if ($res_data->num_rows > 0) {
                                $data = $res_data->fetch_assoc();
                                if ($data['password'] == sha1($_POST['password'])) {
                                    $result = array(
                                        'status' => true,
                                        'msg' => 'password matched'
                                    );
                                    echo json_encode($result);
                                } else {
                                    throw new Exception('Password do not matched');
                                }
                            } else {
                                throw new Exception("User not found");
                            }
                        } else {
                            throw new Exception("Query execution failed");
                        }
                    } else {
                        throw new Exception('Binding parametrs failed');
                    }
                } else {
                    throw new Exception('Prepare statement failed');
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function reset_password() {
        try {
            if (isset($_POST['user_id']) && $_POST['password']) {
                $password = sha1($_POST['password']);
                if ($stmt = $this->con->prepare("UPDATE users SET password =? WHERE id=?")) {
                    if ($stmt->bind_param('si', $password, $_POST['user_id'])) {
                        if ($stmt->execute()) {
                            $result = array(
                                'status' => true,
                                'msg' => 'password updated'
                            );
                            echo json_encode($result);
                        } else {
                            throw new Exception("Query execution failed");
                        }
                    } else {
                        throw new Exception('Binding parametrs failed');
                    }
                } else {
                    throw new Exception('Prepare statement failed');
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function creat_password() {
        try {
            if (isset($_POST['user_id']) && $_POST['password']) {
                $password = sha1($_POST['password']);
                if ($stmt = $this->con->prepare("UPDATE users SET password =? WHERE id=?")) {
                    if ($stmt->bind_param('si', $password, $_POST['user_id'])) {
                        if ($stmt->execute()) {
                            $result = array(
                                'status' => true,
                                'msg' => 'password  created'
                            );
                            echo json_encode($result);
                        } else {
                            throw new Exception("Query execution failed");
                        }
                    } else {
                        throw new Exception('Binding parametrs failed');
                    }
                } else {
                    throw new Exception('Prepare statement failed');
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function update_profile_pic() {
        try {
            if (isset($_POST['profile_pic']) && isset($_POST['user_id'])) {
                $user_exist = self::user_exist_fun_id($_POST['user_id']);
                //print_r($user_exist);
                if (empty($_POST['profile_pic'])) {
                    $profile_pic = '';
                } else {
                    $profile_pic = self::upload_pic($_POST['profile_pic']);
                }
                if ($user_exist['status'] == false) {
                    throw new Exception("User does't exist");
                }
                if (empty($profile_pic)) {
                    $profile_pic = '';
                } else {
                    if (!empty($user_exist['data']['profile_pic'])) {
                        unlink(MEDIA_DIR . DS . $user_exist['data']['profile_pic']);
                    }
                }
                if (empty($profile_pic) || $profile_pic == '') {
                    $res_pic = '';
                } else {
                    $res_pic = WEB_MEDIA . DS . $profile_pic;
                }
                if ($stmt = $this->con->prepare("UPDATE users SET profile_pic=? WHERE id=?")) {
                    if ($stmt->bind_param('si', $profile_pic, $_POST['user_id'])) {
                        if ($stmt->execute()) {
                            $result = array(
                                'status' => true,
                                'profile_pic' => $res_pic,
                                'msg' => 'Profic pic updated successfully'
                            );
                            echo json_encode($result);
                        } else {
                            throw new Exception("Query Execution failed");
                        }
                    } else {
                        throw new Exception("Query binding failed");
                    }
                } else {
                    throw new Exception("Prepare Statement failed");
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function profile_update() {
        try {
            if (isset($_POST['user_id'])) {
                $user_exist = self::user_exist_fun_id($_POST['user_id']);
                //print_r($user_exist);
                $update = '';
                if (isset($_POST['first_name'])) {
                    $value = $_POST['first_name'];
                    $field = 'first_name';
                } elseif (isset($_POST['last_name'])) {
                    $value = $_POST['last_name'];
                    $field = 'last_name';
                } elseif (isset($_POST['dob'])) {
                    $value = $_POST['dob'];
                    $field = 'dob';
                } elseif (isset($_POST['education_level'])) {
                    $value = $_POST['education_level'];
                    $field = 'education_level';
                } elseif (isset($_POST['mobile_number'])) {
                    $value = $_POST['mobile_number'];
                    $field = 'mobile_number';
                } elseif (isset($_POST['gender'])) {
                    $value = $_POST['gender'];
                    $field = 'gender';
                } elseif (isset($_POST['city'])) {
                    $value = $_POST['city'];
                    $field = 'city';
                }
                $query = "UPDATE users SET " . $field . "=? WHERE id=?";
                if ($stmt = $this->con->prepare($query)) {
                    if ($stmt->bind_param('si', $value, $_POST['user_id'])) {
                        if ($stmt->execute()) {
                            $result = array(
                                'status' => true,
                                'msg' => 'Profile field updated'
                            );
                            echo json_encode($result);
                        } else {
                            throw new Exception("Query Execution failed");
                        }
                    } else {
                        throw new Exception("Query binding failed");
                    }
                } else {
                    throw new Exception("Prepare Statement failed");
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function home_banner() {
        try {
            if ($stmt = $this->con->prepare("SELECT * FROM banner")) {
                if ($stmt->execute()) {
                    $data = array();
                    $rows = $stmt->get_result();
                    while ($row = $rows->fetch_assoc()) {
                        if ($row['img'] != '' || !empty($row['img'])) {
                            $row['img'] = WEB_MEDIA . DS . $row['img'];
                        }
                        $data[] = $row;
                    }
                    $result = array(
                        'status' => true,
                        'data' => $data
                    );
                    echo json_encode($result);
                } else {
                    throw new Exception("Query execution failed");
                }
            } else {
                throw new Exception("Query prepare failed");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function get_education_level() {
        try {
            if (isset($_POST['user_id'])) {
                $user_id = $_POST['user_id'];
                $query = "SELECT IF(education_level!='',education_level,'') AS education_level FROM users WHERE id='" . $user_id . "'";
                if ($exist = $this->con->query($query)) {
                    if ($exist->num_rows > 0) {
                        $user = $exist->fetch_assoc();

                        $result = array(
                            'status' => true,
                            'education_level' => $user['education_level'],
                        );
                        echo json_encode($result);
                    } else {
                        throw new Exception("User not found");
                    }
                } else {
                    throw new Exception("db query failed");
                }
            } else {
                throw new Exception("user_id field is missing");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

     public function payUhash() {
            try {
                if (isset($_POST['RazSignature']) && isset($_POST['PaymentId']) && isset($_POST['OrderId']) &&  isset($_POST['user_id']) && isset($_POST['amount']) && isset($_POST['productInfo']) && isset($_POST['firstName']) && isset($_POST['email']) && isset($_POST['udf1']) && isset($_POST['udf2']) && isset($_POST['udf3']) && isset($_POST['udf4']) && isset($_POST['udf5'])) {
                    $key = "hRZ1gzXY";
                    $salt = "2Czbr3Kcb6";
                    $status = 'Success';
                    $end = date('Y-m-d', strtotime('+1 years'));
                    $query = "INSERT INTO payment (PaymentId,OrderId,user_id,amount,productInfo,email,firstname,udf1,udf2,udf3,udf4,udf5,status,RazSignature,validate_till) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    if ($stmt = $this->con->prepare($query)) {
                        if ($stmt->bind_param("ssissssssssssss", $_POST['PaymentId'],$_POST['OrderId'] ,$_POST['user_id'], $_POST['amount'], $_POST['productInfo'], $_POST['email'], $_POST['firstName'], $_POST['udf1'], $_POST['udf2'], $_POST['udf3'], $_POST['udf4'], $_POST['udf5'], $status, $_POST['RazSignature'], $end)) {
                            if ($stmt->execute()) {
                                $arr = array(
                                    'status' => true,
                                );
                                echo json_encode($arr);
                            } else {
                                throw new Exception("Query execution failed");
                            }
                        } else {
                            throw new Exception("query binding failed");
                        }
                    } else {
                        throw new Exception("prepare statement failed");
                    }
                } else {
                    throw new Exception("missing required field");
                }
                $stmt->close();
            } catch (Exception $ex) {
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
                die();
            }
     }

    public function payUsuccess() {
        $key = "hRZ1gzXY";
        $salt = "2Czbr3Kcb6";
        $status = $_POST['status'];
        $txnId = $_POST['txnid'];
        $hash = $_POST['hash'];
        $productinfo = $_POST['productinfo'];
        $email = $_POST['email'];
        $payumoneyid = $_POST['payuMoneyId'];
        $payhash_str = $salt . '|' . $status . '|' . self::checkNull($_POST['udf10']) . '|' . self::checkNull($_POST['udf9']) . '||||||||' . self:: checkNull($_POST['udf1']) . '|' . self:: checkNull($_POST['email']) . '|' . self::checkNull($_POST['firstname']) . '|' . self::checkNull($_POST['productinfo']) . '|' . self::checkNull($_POST['amount']) . '|' . self::checkNull($_POST['txnid']) . '|' . $key;
        $validate = 1;
        $pay_success = 1;
        $payhash = strtolower(hash('sha512', $payhash_str));
        if ($payhash != $hash) {
            $validate = 0;
        } else {
            $services = credit_service($email);
        }
        $query = "UPDATE payment SET  status=?, payUmoney_id=?, success=?, validate=? WHERE txn_id=?";
        $stmt = $this->con->prepare($query);
        $stmt->bind_param("ssiss", $status, $payumoneyid, $pay_success, $validate, $txnId);
        $stmt->execute();
        $stmt->close();
    }

    public function payUcancel() {
        $key = "hRZ1gzXY";
        $salt = "2Czbr3Kcb6";
        $status = $_POST['status'];
        $txnId = $_POST['txnid'];
        $hash = $_POST['hash'];
        $productinfo = $_POST['productinfo'];
        $email = $_POST['email'];
        $payumoneyid = $_POST['payuMoneyId'];
        $payhash_str = $salt . '|' . $status . '|' . self::checkNull($_POST['udf10']) . '|' . self::checkNull($_POST['udf9']) . '||||||||' . self:: checkNull($_POST['udf1']) . '|' . self:: checkNull($_POST['email']) . '|' . self::checkNull($_POST['firstname']) . '|' . self::checkNull($_POST['productinfo']) . '|' . self::checkNull($_POST['amount']) . '|' . self::checkNull($_POST['txnid']) . '|' . $key;
        $payhash = strtolower(hash('sha512', $payhash_str));
        $validate = 1;
        $pay_success = 0;
        if ($payhash != $hash) {
            $validate = 0;
        }
        $query = "UPDATE payment SET  status=?, payUmoney_id=?, success=?, validate=? WHERE txn_id=?";
        $stmt = $this->con->prepare($query);
        $stmt->bind_param("ssiss", $status, $payumoneyid, $pay_success, $validate, $txnId);
        $stmt->execute();
        $stmt->close();
    }

    public function checkNull($value) {
        if ($value == null) {
            return '';
        } else {
            return $value;
        }
    }

    public function payments_plans() {
        try {
            $query = "SELECT id, name, amount, active FROM pay_plans";
            if ($stmt = $this->con->prepare($query)) {
                if ($stmt->execute()) {
                    $plans = array();
                    $rows = $stmt->get_result();
                    $services_id = array();

                    while ($row = $rows->fetch_assoc()) {
                        if ($stmt1 = $this->con->prepare("SELECT plan_services.id, plan_services.title, plan_services.description,
                            IF(plan_service_meta.quantity='','',CONCAT(IF(plan_service_meta.quantity=-1,'Unlimited',plan_service_meta.quantity),if(plan_service_meta.prefix='','',' '),plan_service_meta.prefix)) AS quantity, plan_service_meta.place AS quantity_place FROM plan_service_meta JOIN plan_services ON plan_services.id=plan_service_meta.plan_service WHERE plan_service_meta.pay_plan=? ORDER BY plan_services.id ASC")) {
                            if ($stmt1->bind_param('i', $row['id'])) {
                                if ($stmt1->execute()) {
                                    $st_rows = $stmt1->get_result();
                                    while ($st_row = $st_rows->fetch_assoc()) {
                                        if (in_array($st_row['id'], $services_id) || empty($plans)) {
                                            $color_code = '#000000';
                                        } else {
                                            $color_code = '#2E8B57';
                                        }
                                        $services_id[] = $st_row['id'];
                                        $st_row['color_code'] = $color_code;
                                        $row['services'][] = $st_row;
                                    }
                                } else {
                                    throw new Exception("query execution failed");
                                }
                            } else {
                                throw new Exception("paramter binding failed");
                            }
                        } else {
                            throw new Exception("prepare statement failed sub");
                        }
                        $plans[] = $row;
                    }
                    $result = array(
                        'status' => true,
                        'data' => $plans
                    );
                    echo json_encode($result);
                } else {
                    throw new Exception("Query execution failed");
                }
            } else {
                throw new Exception("prepare statement failed");
            }
            $stmt->close();
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function additional_services() {
        try {
            $query = "SELECT id, title, description, amount FROM additional_services";
            if ($stmt = $this->con->prepare($query)) {
                if ($stmt->execute()) {
                    $plans = array();
                    $rows = $stmt->get_result();
                    while ($row = $rows->fetch_assoc()) {
                        $plans[] = $row;
                    }
                    $result = array(
                        'status' => true,
                        'data' => $plans
                    );
                    echo json_encode($result);
                } else {
                    throw new Exception("Query execution failed");
                }
            } else {
                throw new Exception("prepare statement failed");
            }
            $stmt->close();
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }
    public function getRealIpAddr()
    {
        if(!empty($_SERVER['HTTP_CLIENT_IP']))
        {
            $ip=$_SERVER['HTTP_CLIENT_IP'];
        }
        elseif(!empty($_SERVER['HTTP_X_FORWARDED_FOR']))
        {
            $ip=$_SERVER['HTTP_X_FORWARDED_FOR'];
        }
        else
        {
            $ip=$_SERVER['REMOTE_ADDR'];
        }
        return $ip;
    }
    public function sendMsgNational($mobile, $msg) {
        $url = "http://api.msg91.com/api/sendhttp.php?sender=MSGIND&route=4&mobiles=" . $mobile . "&authkey=147771Atm9yqVFOHnS58e4e32d&country=91&message=" . $msg;
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = curl_exec($ch);
        $result = (array) json_decode($result);
        curl_close($ch);
        return $result;
        // return $sms_send = file_get_contents($url);

    }

    public function sendMsgInt($mobile, $msg, $contry_code) {
        $url = "http://api.msg91.com/api/sendhttp.php?sender=MSGIND&route=4&mobiles=" . $mobile . "&authkey=147771Atm9yqVFOHnS58e4e32d&country=" . $contry_code . "&message=" . $msg;
         $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $result = curl_exec($ch);
        $result = (array) json_decode($result);
        curl_close($ch);
        return $result;
        // return $sms_send = file_get_contents($url);
    }

    public function sendOtp() {
        try {

            if (isset($_POST['mobile']) && isset($_POST['country_code']) && isset($_POST['otp'])) {
                $msg = urlencode($_POST['otp'] . " is the OTP for verification of your mobile number on CareerGuide.");
                $msgsend = '';
                switch ($_POST['country_code']) {
                    case '91':
                        $msgsend = self::sendMsgNational($_POST['mobile'], $msg);
                        break;
                    case '+91':
                        $msgsend = self::sendMsgNational($_POST['mobile'], $msg);
                        break;
                    case 91:
                        $msgsend = self::sendMsgNational($_POST['mobile'], $msg);
                        break;
                    case '(+91)':
                        $msgsend = self::sendMsgNational($_POST['mobile'], $msg);
                        break;
                    case (+91):
                        $msgsend = self::sendMsgNational($_POST['mobile'], $msg);
                        break;
                    case 1:
                        $msgsend = self::sendMsgInt($_POST['mobile'], $msg, 1);
                        break;

                    case '1':
                        $msgsend = self::sendMsgInt($_POST['mobile'], $msg, 1);
                        break;
                    case '+1':
                        $msgsend = self::sendMsgInt($_POST['mobile'], $msg, 1);
                        break;
                    case '(+1)':
                        $msgsend = self::sendMsgInt($_POST['mobile'], $msg, 1);
                        break;
                    case (+1):
                        $msgsend = self::sendMsgInt($_POST['mobile'], $msg, 1);
                        break;
                    default:
                        $msgsend = self::sendMsgInt($_POST['mobile'], $msg, 0);
                        break;
                }
                $result = array(
                    'status' => true,
                    'msg' => 'otp sent'
                );
                echo json_encode($result);
            } else {
                throw new Exception('Missing required field');
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function __destruct() {
        $this->con->close();
        $this->con = NULL;
    }

    public function flag_cal() {
        try {
            if ($IpAddr = self::getRealIpAddr()) {
                if ($IpDetail = file_get_contents("http://www.geoplugin.net/json.gp?ip=" . $IpAddr)) {
                    $IpDetail = json_decode($IpDetail);
                    if (!empty($IpDetail->geoplugin_countryName)) {
                        $query = "SELECT id FROM country WHERE name LIKE '" . $IpDetail->geoplugin_countryName."' ";
                        if ($result = $this->con->query($query)) {
                            $phone_code = $result->fetch_assoc();
                        }
                    }
                }
            }
            $query = "SELECT id,nicename AS name,flag, phonecode AS code FROM country ORDER BY phonecode ASC";
            if ($stmt = $this->con->prepare($query)) {
                if ($stmt->execute()) {
                    $flag = array();
                    $rows = $stmt->get_result();
                    while ($row = $rows->fetch_assoc()) {
                        $row['selected'] = FALSE;
                        if(isset($phone_code['id']))
                        {
                            if($phone_code['id'] == $row['id'])
                            {
                                $row['selected'] = TRUE;
                            }
                        }
                        $row['flag'] = WEB_MEDIA . DS . 'flags' . DS . $row['flag'];
                        $flag[] = $row;
                    }
                    $result = array(
                        'status' => true,
                        'data' => $flag
                    );
                    return $result;
                } else {
                    throw new Exception("Query execution failed");
                }
            } else {
                throw new Exception("prepare statement failed");
            }
            $stmt->close();
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            return $result;
            die();
        }
    }

    public function flags() {
        $result = self::flag_cal();
        echo json_encode($result);
    }

    public function services() {
        try {
            $query = "SELECT * FROM services";
            if ($stmt = $this->con->prepare($query)) {
                if ($stmt->execute()) {
                    $service = array();
                    $rows = $stmt->get_result();
                    while ($row = $rows->fetch_assoc()) {
                        $stmt_one = $this->con->prepare("SELECT * FROM services_meta WHERE service_id='" . $row['id'] . "'");
                        $stmt_one->execute();
                        $meta_rows = $stmt_one->get_result();
                        while ($meta_row = $meta_rows->fetch_assoc()) {
                            $row[$meta_row['meta_key']] = $meta_row['meta_value'];
                        }
                        $service[] = $row;
                    }
                    $result = array(
                        'status' => true,
                        'data' => $service
                    );
                    echo json_encode($service);
                } else {
                    throw new Exception("Query execution failed");
                }
            } else {
                throw new Exception("prepare statement failed");
            }
            $stmt->close();
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            return $result;
            die();
        }
    }

    public function credit_service($email) {
        $services = array(
            array('id' => 1, 'quantity' => 1, 'expiry' => '9999-12-31'),
            array('id' => 2, 'quantity' => 1, 'expiry' => '9999-12-31')
        );
        $user = self::user_exist_fun($email);
        $user_id = $user['data']['id'];
        $query = "INSERT INTO inventry (email,user_id,service_id,quantity,expiry) VALUES(?,?,?,?,?)";
        $stmt = $this->con->prepare($query);
        foreach ($services as $key => $value) {
            $stmt->bind_param('siiss', $email, $user_id, $value['id'], $value['quantity'], $value['expiry']);
            $stmt->execute();
        }
        $stmt->close();
        return $services;
    }

    public function user_inventry() {
        try {

            if (isset($_POST['user_id'])) {
                $query = "SELECT inventry.*,services.name FROM inventry JOIN services ON inventry.service_id=services.id WHERE inventry.user_id='" . $_POST['user_id'] . "' AND inventry.quantity>0 AND expiry>=CURDATE()";
                if ($stmt = $this->con->prepare($query)) {
                    if ($stmt->execute()) {
                        $service = array();
                        $rows = $stmt->get_result();
                        while ($row = $rows->fetch_assoc()) {
                            $service[] = $row;
                        }
                    } else {
                        throw new Exception("Query execution failed");
                    }
                } else {
                    throw new Exception("prepare statement failed");
                }
                $result = array(
                    'status' => true,
                    'data' => $service
                );
                echo json_encode($result);
                $stmt->close();
            } else {
                throw new Exception('Missing required field');
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function counsellor_info_update() {
        try {
            if (isset($_POST['c_id'])) {
                $id = $_POST['c_id'];
                unset($_POST['c_id']);
                $find = $this->con->query("SELECT id FROM counsellor_info WHERE c_id='" . $id . "'");
                if (isset($_POST['dob'])) {
                    if ($find->num_rows > 0) {
                        $query = "UPDATE counsellor_info SET dob = '" . $_POST['dob'] . "' ";
                    } else {
                        echo $query = "INSERT INTO counsellor_info (c_id,dob) VALUES('" . $id . "','" . $_POST['dob'] . "') ";
                    }
                    $stmt = $this->con->query($query);
                    unset($_POST['dob']);
                }
                if (count($_POST) > 0) {
                    foreach ($_POST as $key => $value) {
                        $f = $this->con->query("SELECT id FROM counsellor_meta WHERE meta_key LIKE '" . $key . "' AND c_id='" . $id . "'");
                        if ($f->num_rows > 0) {
                            $data = $f->fetch_assoc();
                            $query = "UPDATE counsellor_meta SET meta_value='" . $value . "' WHERE id='" . $data['id'] . "' AND meta_key LIKE '" . $key . "'";
                        } else {
                            $query = "INSERT INTO counsellor_meta(c_id,meta_key,meta_value) VALUES ('" . $id . "','" . $key . "','" . $value . "')";
                        }
                        $stmt2 = $this->con->query($query);
                    }
                }
                $result = array(
                    'status' => true,
                    'msg' => 'Info updated'
                );
                echo json_encode($result);
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function counsellor_info() {
        try {
            if (isset($_POST['c_id'])) {
                $counsellor = $this->con->query("SELECT * FROM counsellor LEFT JOIN counsellor_info ON counsellor_info.c_id=counsellor.id WHERE counsellor.id='" . $_POST['c_id'] . "'");
                if ($counsellor->num_rows > 0) {
                    $row = $counsellor->fetch_assoc();

                    $consellor_meta = $this->con->query("SELECT * FROM counsellor_meta WHERE c_id='" . $_POST['c_id'] . "'");
                    while ($cos = $consellor_meta->fetch_assoc()) {
                        $row[$cos['meta_key']] = $cos['meta_value'];
                    }
                    $result = array(
                        'status' => true,
                        'data' => $row
                    );
                    echo json_encode($result);
                } else {
                    throw new Exception("Counsellor not found");
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
            die();
        }
    }

    public function test() {
        $txnId = '1015257629615af14b911b961';
        $query1 = "SELECT payhash FROM payment WHERE txn_id=?";
        $stmt1 = $this->con->prepare($query1);
        $stmt1->bind_param("s", $txnId);
        $stmt1->execute();
        $res = $stmt1->get_result();
        $row = $res->fetch_assoc();
        $payhash = $row['payhash'];
        $stmt1->close();
        $status = 'testing';
        $payUmoney_id = 'payuid';
        $validate = 0;
        $query = "UPDATE payment SET  status=?, payUmoney_id=?, validate=? WHERE txn_id=?";
        $stmt = $this->con->prepare($query);
        $stmt->bind_param("ssss", $status, $payumoneyid, $validate, $txnId);
        $stmt->execute();
    }

    public function counsellor_register() {
        try {
            if (isset($_POST['first_name']) && isset($_POST['last_name']) && isset($_POST['profile_pic']) && isset($_POST['gender']) && isset($_POST['dob']) && isset($_POST['location']) && isset($_POST['student_edu_level'])) {
                if (!empty($_POST['profile_pic'])) {
                    $profile_pic = self::upload_pic($_POST['profile_pic']);
                } else {
                    $profile_pic = '';
                }
                if (!($stmt = $this->con->prepare("INSERT INTO co_user (first_name,last_name,profile_pic,gender,dob,location,student_edu_level) VALUES(?,?,?,?,?,?,?)"))) {
                    throw new Exception("Prepare failed: (" . $this->con->errno . ") " . $this->con->error);
                }
                if (!$stmt->bind_param("sssssss", $_POST['first_name'], $_POST['last_name'], $profile_pic, $_POST['gender'], $_POST['dob'], $_POST['location'], $_POST['student_edu_level'])) {
                    throw new Exception("Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error);
                }
                if (!$stmt->execute()) {
                    throw new Exception("Execute failed: (" . $stmt->errno . ") " . $stmt->error);
                }
                $result = array(
                    'status' => true,
                    'co_id' => $stmt->insert_id
                );
                echo json_encode($result);
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
        }
    }

    public function counsellor_profile() {
        try {
            if (isset($_POST['co_id'])) {
                if (!($stmt = $this->con->prepare("SELECT * FROM co_user WHERE id=?"))) {
                    throw new Exception("Prepare failed: (" . $this->con->errno . ") " . $this->con->error);
                }
                if (!$stmt->bind_param("i", $_POST['co_id'])) {
                    throw new Exception("Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error);
                }
                if (!$stmt->execute()) {
                    throw new Exception("Execute failed: (" . $stmt->errno . ") " . $stmt->error);
                }
                $rows = $stmt->get_result();
//                print_r($stmt);
                if ($rows->num_rows > 0) {

                    $data = $rows->fetch_assoc();
                    $result = array(
                        'status' => true,
                        'data' => $data
                    );
                    echo json_encode($result);
                } else {
                    throw new Exception('Data not found');
                }

            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
        }
    }

    public function update_counsellor_profile() {
        try {
            if (isset($_POST['co_id']) ) {
                $str = "UPDATE co_user SET ";
                if(isset($_POST['first_name']))
                {
                   $str .= " first_name='".$_POST['first_name']."' ";
                }
                if(isset($_POST['last_name']))
                {
                   $str .= " last_name='".$_POST['last_name']."' ";
                }
                if(isset($_POST['gender']))
                {
                   $str .= " gender='".$_POST['gender']."' ";
                }
                if(isset($_POST['dob']))
                {
                   $str .= " dob='".$_POST['dob']."' ";
                }
                if(isset($_POST['location']))
                {
                   $str .= " location='".$_POST['location']."' ";
                }
                if(isset($_POST['student_edu_level']))
                {
                   $str .= " student_edu_level='".$_POST['student_edu_level']."' ";
                }
                if (isset($_POST['profile_pic']))
                {
                    if(!empty($_POST['profile_pic']))
                    {
                        $profile_pic = self::upload_pic($_POST['profile_pic']);
                    } else {
                        $profile_pic = '';
                    }
                    $str .= " profile_pic='".$profile_pic."' ";
                }
                $str .= " WHERE id=?";
                if (!($stmt = $this->con->prepare($str))) {
                    throw new Exception("Prepare failed: (" . $this->con->errno . ") " . $this->con->error);
                }
                if (!$stmt->bind_param("i", $_POST['co_id'])) {
                    throw new Exception("Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error);
                }
                if (!$stmt->execute()) {
                    throw new Exception("Execute failed: (" . $stmt->errno . ") " . $stmt->error);
                }
                $result = array(
                    'status' => true,
                    'msg' => "Profile updated successfully"
                );
                echo json_encode($result);
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
        }
    }
    public function education_level()
    {
      try{
        $query = "SELECT * FROM education";
        if(!($stmt = $this->con->prepare("SELECT * FROM education_level")))
        {
          throw new Exception("Query prepare Failed");
        }
        if(!$stmt->execute())
        {
          throw new Exception("Query Execution failed");
        }
        if(!$rows = $stmt->get_result())
        {
          throw new Exception("Result not found");
        }
        while($row = $rows->fetch_assoc())
        {
          $data[] = $row;
        }
        $result = array(
            'status' => true,
            'data' => $data
        );
        echo json_encode($result);
      } catch (Exception $ex) {
          $result = array(
              'status' => false,
              'msg' => $ex->getMessage()
          );
          echo json_encode($result);
      }
    }
    public function all_counsellors()
    {
      try{
        // $and ='';
        // if(isset($_POST['city']))
        // {
        //   $and .= ' AND ( ';
        //     $start = 0;
        //   $city = json_decode($_POST['city']);
        //   foreach ($city as $key => $value) {
        //   if($start != 0)
        //   {
        //     $and .= ' OR ';
        //   }
        //     $and .=" co_user.city LIKE '%".$value."%'" ;
        //     $start++;
        //   }
        //   $and = ' ) ';
        // }
        // if(isset($_POST['expertise']))
        // {
        //   $and .= ' AND ( ';
        //     $start = 0;
        //   $exp = json_decode($_POST['expertise']);
        //   foreach ($exp as $key => $value) {
        //   if($start != 0)
        //   {
        //     $and .= ' OR ';
        //   }
        //     $and .=" co_expertise.fields LIKE '%".$value."%'" ;
        //     $start++;
        //   }
        //   $and = ' )';
        // }
        $query = "SELECT co_user.id AS co_id,co_user.email,co_user.first_name,co_user.last_name,co_user.profile_pic,co_user.dob,co_user.gender,co_user.city,co_user.education_level,co_user.student_education_level,co_user.about,co_bio.headline,(SELECT AVG(rating) FROM co_ratings WHERE co_id=co_user.id) AS ratings,co_expertise.fields AS fields FROM co_user LEFT JOIN co_bio ON co_user.id=co_bio.user_id LEFT JOIN co_expertise ON co_expertise.user_id=co_user.id ";
        if(!$stmt = $this->con->prepare($query))
        {
          throw new Exception("Prepare statement failed : ".$this->con->error);
        }
        if(!$stmt->execute())
        {
          throw new Exception("Query execution failed : ".$stmt->error);
        }
        $rows = $stmt->get_result();
        $counsellor = array();
        while($row = $rows->fetch_assoc())
        {
          $row['student_education_level'] = json_decode($row['student_education_level']);
          $row['fields'] = json_decode($row['fields']);
          $row['profile_pic'] = empty($row['profile_pic'])?'':WEB_MEDIA.DS.$row['profile_pic'];
          $counsellor[] = $row;
        }
        $result = array(
          'status'=>true,
          'counsellors'=>$counsellor
        );
        echo json_encode($result);
        $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
    }
    public function ratings()
    {
      try{
        if(isset($_POST['user_id']) && isset($_POST['co_id']) && isset($_POST['rating']))
        {
          if(!($check = $this->con->query("SELECT * FROM co_ratings WHERE user_id='".$_POST['user_id']."' AND co_id='".$_POST['co_id']."'")))
          {
            throw new Exception("Query execution failed : ".$this->con->error);
          }
          if($check->num_rows>0)
          {
            $query = "UPDATE co_ratings SET rating='".$_POST['rating']."' WHERE user_id='".$_POST['user_id']."' AND co_id='".$_POST['co_id']."'";
          }
          else {
            $query ="INSERT INTO co_ratings(user_id,co_id,rating) VALUES('".$_POST['user_id']."','".$_POST['co_id']."','".$_POST['rating']."')";
          }
          if(!($stmt = $this->con->prepare($query)))
          {
            throw new Exception("Prepare statement failed : ".$this->con->error);
          }
          if(!$stmt->execute())
          {
            throw new Exception("Query execution failed : ".$stmt->error);
          }
          $result = array(
            'status'=>true,
            'msg'=>'Rated successfully'
          );
          echo json_encode($result);
        }
        else {
          throw new Exception("Missing required field");
        }
      }
      catch(Exception $ex)
      {
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
    }
    public function sendPushNotification($fields) {
        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = json_encode($fields);
        $headers = array(
            'Authorization: key=' . "AAAAch54rh8:APA91bEFoz-2wAzqqkQHR7UTqTIB1PM1v5MDUWRhzqdnqwvxDgwdUdUPRZ-7Jtrna0DzlgyJjAcra8alK8AgRTA9wCztl1NLzex25BCR26qDRZcB4IXC1_Ebb0LsJmuWIbifdhWOTuWWoxRht9BJd1mKF0e0lVnxcQ",
            'Content-Type: application/json'
        );
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
        $result = curl_exec($ch);
        $result = (array) json_decode($result);
        curl_close($ch);
        return $result;
    }

    public function chat_msg($user_id, $notify_type, $inserted_id, $msg, $image, $chat_type, $co_id) {
        $user = self::get_user('id', $user_id);
        $user = $user['row'];
        $profile_pic = $user['profile_pic'] != '' ? $user['profile_pic'] : '';
        $image = empty($image)?'':WEB_MEDIA.DS.$image;
        $date = date('Y-m-d H:i:s');
        $fields = array(
            'to' => "/topics/cgc" . $co_id,
            'data' => array(
                'id' => $inserted_id,
                'noti_type' => $notify_type,
                'user_id' => $user_id,
                'user_name' => $user['first_name'] . ' ' . $user['last_name'],
                'profile_pic' => $profile_pic,
                'chat_id' => $inserted_id,
                'chat_text' => $msg,
                'chat_type' => $chat_type,
                'image' => $image,
                'date'=>$date
            )
        );
        $stat = self::sendPushNotification($fields);
    }

    public function chat_to_counsellor() {
        try {
            if (isset($_POST['user_id']) && isset($_POST['co_id']) && isset($_POST['msg']) && isset($_POST['chat_type'])) {
                $image = '';
                if (isset($_POST['image']) && !empty($_POST['image'])) {
                    $image = self::upload_pic($_POST['image']);
                }
                if ($query = $this->con->query("INSERT INTO chat (user_id,co_id,msg,image,type) VALUES('" . $_POST['user_id'] . "','" . $_POST['co_id'] . "','" . $_POST['msg'] . "','" . $image . "','" . $_POST['chat_type'] . "')")) {
                    $inerted_id = $this->con->insert_id;
                    if (!($query = $this->con->query("INSERT INTO chat_meta_user (chat_id,user_id) VALUES('" . $inerted_id . "','" . $_POST['user_id'] . "') "))) {
                        throw new Exception('Something went wrong : '. $this->con->error);
                    }
                    self::chat_msg($_POST['user_id'], "chat_message", $inerted_id, $_POST['msg'], $image, $_POST['chat_type'], $_POST['co_id']);
                    $result = array(
                        'status' => true,
                        'chat_id'=>$inerted_id,
                        'msg' => 'Msg sended'
                    );
                    if($image != '')
                    {
                      $result['img'] = WEB_MEDIA.DS.$image;
                    }
                    echo json_encode($result);
                } else {
                    throw new Exception("Query failed : " . $this->con->error);
                }
            } else {
                throw new Exception("Missing required field");
            }
        } catch (Exception $ex) {
            $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
        }
    }
    public function get_chat()
   {
       try{
           if(isset($_POST['user_id']) && isset($_POST['co_id']))
           {
             $query = "SELECT status AS online_status,status_time FROM co_user WHERE id='".$_POST['co_id']."'";
             if(!$st = $this->con->query($query))
             {
               throw new Exception("query failed : ".$this->con->error);
             }
             if(!$row_res= $st->fetch_assoc())
             {
               throw new Exception("Query fetch failed : ".$stmt->error);
             }
               $chat_query = "SELECT chat.id AS id,msg,image,type,chat_meta_co.co_id AS co_id,chat_meta_user.user_id AS user_id,chat.date_time AS date_time FROM chat LEFT JOIN chat_meta_co ON chat.id=chat_meta_co.chat_id LEFT JOIN chat_meta_user ON chat.id=chat_meta_user.chat_id WHERE chat.user_id='".$_POST['user_id']."' AND chat.co_id='".$_POST['co_id']."' AND deleted=0 ORDER BY chat.id";
               if(!($query = $this->con->query($chat_query)))
               {
                 throw new Exception('Query execution failed : '.$this->con->error);
               }
               $chat = array();
               while($row = $query->fetch_assoc())
               {
                   if(!empty($row['image']) && $row['image'] != '')
                   {
                       $row['image'] = WEB_MEDIA.DS.$row['image'];
                   }
                   $chat[] = $row;
               }
               $result = array(
                   'status'=>true,
                   'online_status'=>$row_res['online_status'],
                   'status_time'=>$row_res['status_time'],
                   'chat'=>$chat
               );
               echo json_encode($result);
           }
           else
           {
               throw new Exception("Missing required field");
           }
       } catch (Exception $ex) {
           $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
       }
   }
   public function student_typing() {
       try{
           if(isset($_POST['user_id']) && isset($_POST['counselor_id']))
           {

            $fields = array(
                'to' => "/topics/cgc" . $_POST['counselor_id'],
                'data' => array(
                    'id' => 123456,
                    'noti_type' => 'typing',
                    'user_id' => $_POST['user_id']
                )
            );
            $stat = self::sendPushNotification($fields);
           }
           else{
               throw new Exception("missing required field");

           }
       } catch (Exception $ex) {
         $result = array(
                'status' => false,
                'msg' => $ex->getMessage()
            );
            echo json_encode($result);
       }

    }
    public function online_status()
    {
      try{
        if(isset($_POST['user_id']) && isset($_POST['status']))
        {
          $status_query = "UPDATE users SET status='".$_POST['status']."', status_time='".date('Y-m-d H:i:s')."' WHERE id='".$_POST['user_id']."'";
          if(!($query = $this->con->query($status_query)))
          {
            throw new Exception('Query execution failed : '.$this->con->error);
          }
          $result = array(
                 'status' => true,
                 'msg' => 'Status updated'
             );
             echo json_encode($result);
        }else {
          throw new Exception("Missing required field");
        }
      }
      catch(Exception $ex)
      {
        $result = array(
               'status' => false,
               'msg' => $ex->getMessage()
           );
           echo json_encode($result);
      }
    }
public function save_report_url(){ try{
//$url = 'https://s3-ap-southeast-1.amazonaws.com/fal-careerguide/id-la/71181.pdf';
$url = $_POST['report_url'];

$email = $_POST['email']
;

		//$query = "UPDATE users SET report_url='"$url"' "

$url_query = "UPDATE users SET Report_url = '" . $url . "' WHERE email='".$email."'";
if(!($query = $this->con->query($url_query)))
{
throw new Exception('Query execution failed : '.$this->con->error);
}
$result = array(
'status' => true,
'msg' => 'Status updated'
);
echo json_encode($result);
}
catch(Exception $ex)
{
$result = array(
'status' => false,
'msg' => $ex->getMessage()
);
echo json_encode($result);
}
}
public function get_report_url()
{
try{
$email = $_POST['email']
;
$query = "SELECT Report_url from users WHERE email='".$email."'";
if(!($query = $this->con->query($query)))
{
throw new Exception('Query execution failed : '.$this->con->error);
}
$report_url = $query->fetch_assoc();
$result = array(
'status' => true,
'msg' => 'Status updated',
'Report_url' => $report_url['Report_url']
);
echo json_encode($result);

}
catch(Exception $ex)
{
$result = array(
'status' => false,
'msg' => $ex->getMessage()
);
echo json_encode($result);
}
}
public function get_user_name()
    {
      try{
        if(isset($_POST['id']))
        {
          $status_query = "SELECT first_name , last_name from users WHERE id='".$_POST['id']."'";
          if(!($query = $this->con->query($status_query)))
          {
            throw new Exception('Query execution failed : '.$this->con->error);
          }
		  $Details = $query->fetch_assoc();
          $result = array(
                  'status' => true,
                 'msg' => 'Status updated',
				 'first_name' => $Details['first_name'],
				 'last_name' => $Details['last_name'],
             );
             echo json_encode($result);
        }else {
          throw new Exception("Missing required field");
        }
      }
      catch(Exception $ex)
      {
        $result = array(
               'status' => false,
               'msg' => $ex->getMessage()
           );
           echo json_encode($result);
      }
    }
	public function all_available_counsellors()
    {
      try{
        $query = "SELECT co_user.id AS co_id,co_user.email,co_user.first_name,co_user.last_name,co_user.profile_pic,co_user.dob,co_user.gender,co_user.channel_name,co_user.city,co_user.education_level,co_user.topic,co_user.student_education_level,co_user.about,co_bio.headline,(SELECT AVG(rating) FROM co_ratings WHERE co_id=co_user.id) AS ratings,co_expertise.fields AS fields FROM co_user LEFT JOIN co_bio ON co_user.id=co_bio.user_id LEFT JOIN co_expertise ON co_expertise.user_id=co_user.id WHERE is_live=1";
        if(!$stmt = $this->con->prepare($query))
        {
          throw new Exception("Prepare statement failed : ".$this->con->error);
        }
        if(!$stmt->execute())
        {
          throw new Exception("Query execution failed : ".$stmt->error);
        }
        $rows = $stmt->get_result();
        $counsellor = array();
        while($row = $rows->fetch_assoc())
        {
          $row['student_education_level'] = json_decode($row['student_education_level']);
          $row['fields'] = json_decode($row['fields']);
          $row['profile_pic'] = empty($row['profile_pic'])?'':WEB_MEDIA.DS.$row['profile_pic'];
          $counsellor[] = $row;
        }
        $result = array(
          'status'=>true,
          'counsellors'=>$counsellor
        );
        echo json_encode($result);
        $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
    }


public function Facebook_Live_video()
    {
      try{
        if(isset($_POST['uid']))
        {
            $query = "SELECT * FROM Facebook_Live where Video_category ='".$_POST['uid']."' ORDER BY id DESC ";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $counsellor = array();
           while($row = $rows->fetch_assoc())
            {
            $counsellor[] = $row;
            }
            $result = array(
            'status'=>true,
            'counsellors'=>$counsellor
            );
            echo json_encode($result);
            $stmt->close();
        }
        else {
            throw new Exception("Missing required field");
          }
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
    }

public function Fetch_category()
    {
	try{
			$query = "SELECT * FROM education_category";
			if(!$stmt = $this->con->prepare($query))
			{
			  throw new Exception("Prepare statement failed : ".$this->con->error);
			}
			if(!$stmt->execute())
			{
			  throw new Exception("Query execution failed : ".$stmt->error);
			}
			$rows = $stmt->get_result();
			$Category = array();
			while($row = $rows->fetch_assoc())
			{
			  $category[] = $row;
			}
			$result = array(
			  'status'=>true,
			  'category'=>$category
			);
			echo json_encode($result);
			$stmt->close();
		}
		  catch(Exception $ex){
			$result = array(
				'status' => false,
				'msg' => $ex->getMessage()
			);
			echo json_encode($result);
		}
    }

public function Fetch_sub_category()
    {
	try{
		if(isset($_POST['cat_uid']))
        {
			$query = "SELECT * FROM sub_category where uid = '".$_POST['cat_uid']."'";
			if(!$stmt = $this->con->prepare($query))
			{
			  throw new Exception("Prepare statement failed : ".$this->con->error);
			}
			if(!$stmt->execute())
			{
			  throw new Exception("Query execution failed : ".$stmt->error);
			}
			$rows = $stmt->get_result();
			$Category = array();
			while($row = $rows->fetch_assoc())
			{
			  $category[] = $row;
			}
			$result = array(
			  'status'=>true,
			  'category'=>$category
			);
			echo json_encode($result);
			$stmt->close();

		}else {
			throw new Exception("Missing required field");
			}

	   }
		catch(Exception $ex){
			$result = array(
				'status' => false,
				'msg' => $ex->getMessage()
			);
			echo json_encode($result);
		}

	}


public function Fetch_Topics()
    {
			try{
				if(isset($_POST['user_education']))
				{
					$query = "SELECT * FROM sub_topic_cat WHERE parent_uid = '".$_POST['user_education']."'";
					if(!$stmt = $this->con->prepare($query))
					{
					  throw new Exception("Prepare statement failed : ".$this->con->error);
					}

					if(!$stmt->execute())
					{
					  throw new Exception("Query execution failed : ".$stmt->error);
					}
					$rows = $stmt->get_result();
					$topics = array();
					while($row = $rows->fetch_assoc())
					{
					  $topics[] = $row;
					}
					$result = array(
					  'status'=>true,
					  'topics'=>$topics
					);
					echo json_encode($result);
					$stmt->close();
			   }else {
					  throw new Exception("Missing required field");
					}
			}
				catch(Exception $ex){
					$result = array(
						'status' => false,
						'msg' => $ex->getMessage()
					);
					echo json_encode($result);
				}
	}


public function category_counsellors()
    {
      try{
        $query = "SELECT co_user.id AS co_id,co_user.email,co_user.first_name,co_user.last_name,co_user.profile_pic,co_user.dob,co_user.gender,co_user.videocall_channel_name,co_user.city,co_user.education_level,co_user.student_education_level,co_user.about,co_bio.headline,(SELECT AVG(rating) FROM co_ratings WHERE co_id=co_user.id) AS ratings,co_expertise.fields AS fields FROM co_user LEFT JOIN co_bio ON co_user.id=co_bio.user_id LEFT JOIN co_expertise ON co_expertise.user_id=co_user.id WHERE is_available=1";
        if(!$stmt = $this->con->prepare($query))
        {
          throw new Exception("Prepare statement failed : ".$this->con->error);
        }
        if(!$stmt->execute())
        {
          throw new Exception("Query execution failed : ".$stmt->error);
        }
        $rows = $stmt->get_result();
        $counsellor = array();
        while($row = $rows->fetch_assoc())
        {
          $row['student_education_level'] = json_decode($row['student_education_level']);
          $row['fields'] = json_decode($row['fields']);
          $row['profile_pic'] = empty($row['profile_pic'])?'':WEB_MEDIA.DS.$row['profile_pic'];
          $counsellor[] = $row;
        }
        $result = array(
          'status'=>true,
          'counsellors'=>$counsellor
        );
        echo json_encode($result);
        $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
    }

 public function GetCounsellor()
    {
      try{
          
		if(isset($_POST['Uid']))
        {
        $query = "SELECT co_user.id AS co_id,co_user.email,co_user.first_name,co_user.last_name,co_user.profile_pic,co_user.dob,
        co_user.gender,co_user.videocall_channel_name,co_user.city,co_user.education_level,co_user.student_education_level,
        co_user.about,co_bio.headline,(SELECT AVG(rating) FROM co_ratings WHERE co_id=co_user.id) AS ratings,co_expertise.fields AS
         fields FROM co_user LEFT JOIN co_bio ON co_user.id=co_bio.user_id LEFT JOIN co_expertise ON
        co_expertise.user_id=co_user.id WHERE co_user.id='".$_POST['Uid']."'";
        if(!$stmt = $this->con->prepare($query))
        {
          throw new Exception("Prepare statement failed : ".$this->con->error);
        }
        if(!$stmt->execute())
        {
          throw new Exception("Query execution failed : ".$stmt->error);
        }
        $rows = $stmt->get_result();
        $counsellor = array();
        while($row = $rows->fetch_assoc())
        {
          $row['student_education_level'] = json_decode($row['student_education_level']);
          $row['fields'] = json_decode($row['fields']);
          $row['profile_pic'] = empty($row['profile_pic'])?'':WEB_MEDIA.DS.$row['profile_pic'];
          $counsellor[] = $row;
        }
        $result = array(
          'status'=>true,
          'counsellors'=>$counsellor
        );
        echo json_encode($result);
        $stmt->close();
    }else {
        throw new Exception("Missing required field");
        }
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
    }



public function GetPayment()
{
        try{
            if(isset($_POST['user_id']))
            {
                $query = "SELECT * FROM payment WHERE user_id='".$_POST['user_id']."' ORDER BY id DESC";
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }

                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                $rows = $stmt->get_result();
                $payment = array();
                while($row = $rows->fetch_assoc())
                {
                  $payment[] = $row;
                }
                $result = array(
                  'status'=>true,
                  'payments'=>$payment
                );
                echo json_encode($result);
                $stmt->close();
           }else {
                  throw new Exception("Missing required field");
                }
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}

public function Videos_NINE()
{
      try{
            $query = "Select Facebook_Live.id,user_id,Name,co_user.email,img_url,video_url,title,Video_category,views,profile_pic from co_user,Facebook_Live where co_user.email=Facebook_Live.email and Video_category='NINE' ORDER BY Facebook_Live.id DESC";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $videos = array();
           while($row = $rows->fetch_assoc())
            {
            $videos[] = $row;
            }
            $result = array(
            'status'=>true,
            'videos'=>$videos
            );
            echo json_encode($result);
            $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
}


public function Videos_TEN()
{
      try{
            $query = "Select Facebook_Live.id,user_id,Name,co_user.email,img_url,video_url,title,Video_category,views,profile_pic from co_user,Facebook_Live where co_user.email=Facebook_Live.email and Video_category='TEN' ORDER BY Facebook_Live.id DESC";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $videos = array();
           while($row = $rows->fetch_assoc())
            {
            $videos[] = $row;
            }
            $result = array(
            'status'=>true,
            'videos'=>$videos
            );
            echo json_encode($result);
            $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
}

public function Videos_ELEVEN()
{
      try{
            $query = "Select Facebook_Live.id,user_id,Name,co_user.email,img_url,video_url,title,Video_category,views,profile_pic from co_user,Facebook_Live where co_user.email=Facebook_Live.email and Video_category='ELEVEN' ORDER BY Facebook_Live.id DESC";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $videos = array();
           while($row = $rows->fetch_assoc())
            {
            $videos[] = $row;
            }
            $result = array(
            'status'=>true,
            'videos'=>$videos
            );
            echo json_encode($result);
            $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
}

public function Videos_TWELVE()
{
      try{
            $query = "Select Facebook_Live.id,user_id,Name,co_user.email,img_url,video_url,title,Video_category,views,profile_pic from co_user,Facebook_Live where co_user.email=Facebook_Live.email and Video_category='TWELVE' ORDER BY Facebook_Live.id DESC";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $videos = array();
           while($row = $rows->fetch_assoc())
            {
            $videos[] = $row;
            }
            $result = array(
            'status'=>true,
            'videos'=>$videos
            );
            echo json_encode($result);
            $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
}

public function Videos_GRADUATE()
{
      try{
            $query = "Select Facebook_Live.id,user_id,Name,co_user.email,img_url,video_url,title,Video_category,views,profile_pic from co_user,Facebook_Live where co_user.email=Facebook_Live.email and Video_category='GRADUATE' ORDER BY Facebook_Live.id DESC";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $videos = array();
           while($row = $rows->fetch_assoc())
            {
            $videos[] = $row;
            }
            $result = array(
            'status'=>true,
            'videos'=>$videos
            );
            echo json_encode($result);
            $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
}

public function Videos_POSTGRA()
{
      try{
               $query = "Select Facebook_Live.id,user_id,Name,co_user.email,img_url,video_url,title,Video_category,views,profile_pic from co_user,Facebook_Live where co_user.email=Facebook_Live.email and Video_category='POSTGRA' ORDER BY Facebook_Live.id DESC";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $videos = array();
           while($row = $rows->fetch_assoc())
            {
            $videos[] = $row;
            }
            $result = array(
            'status'=>true,
            'videos'=>$videos
            );
            echo json_encode($result);
            $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
}

public function Videos_WORKING()
{
      try{
            $query = "Select Facebook_Live.id,user_id,Name,co_user.email,img_url,video_url,title,Video_category,views,profile_pic from co_user,Facebook_Live where co_user.email=Facebook_Live.email and Video_category='WORKING' ORDER BY Facebook_Live.id DESC";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $videos = array();
           while($row = $rows->fetch_assoc())
            {
            $videos[] = $row;
            }
            $result = array(
            'status'=>true,
            'videos'=>$videos
            );
            echo json_encode($result);
            $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
}

public function youtube_playlist()
{
        try{
            if(isset($_POST['page']))
            {
                 $query = "SELECT * FROM youtube_playlist WHERE page='".$_POST['page']."' AND sub_cat='".$_POST['sub_cat']."'  ORDER BY id ASC";
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }
	
                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                $rows = $stmt->get_result();
                $playlist = array();
                while($row = $rows->fetch_assoc())  
                {
                  $playlist[] = $row;
                }
                $result = array(
                  'status'=>true,
                  'playlist'=>$playlist
                );
                echo json_encode($result);
                $stmt->close();
           }else {
                  throw new Exception("Missing required field");
                }
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}

public function AllVideos()
    {
      try{
            $query = "SELECT * FROM Facebook_Live ORDER BY id DESC ";
            if(!$stmt = $this->con->prepare($query))
            {
            throw new Exception("Prepare statement failed : ".$this->con->error);
            }
            if(!$stmt->execute())
            {
            throw new Exception("Query execution failed : ".$stmt->error);
            }
            $rows = $stmt->get_result();
            $counsellor = array();
           while($row = $rows->fetch_assoc())
            {
            $counsellor[] = $row;
            }
            $result = array(
            'status'=>true,
            'counsellors'=>$counsellor
            );
            echo json_encode($result);
            $stmt->close();
      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
    }

public function Blog_list()
{
        try{
            if(isset($_POST['category']))
            {
                $query = "SELECT * FROM blog_category WHERE category='".$_POST['category']."'  AND sub_cat ='".$_POST['sub_cat']."' ORDER BY id DESC";
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }

                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                $rows = $stmt->get_result();
                $playlist = array();
                while($row = $rows->fetch_assoc())  
                {
                  $playlist[] = $row;
                }
                $result = array(
                  'status'=>true,
                  'playlist'=>$playlist
                );
                echo json_encode($result);
                $stmt->close();
           }else {
                  throw new Exception("Missing required field");
                }
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}

public function UpdateViews()
{
        try{
            if(isset($_POST['video_id']))
            {
                $query = "UPDATE Facebook_Live set VIEWS = VIEWS +1  WHERE id='".$_POST['video_id']."'";
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }

                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                
                $result = array(
                  'status'=>true,
                  'message'=>"Updated Successfully"
                );
                echo json_encode($result);
                $stmt->close();
           }else {
                  throw new Exception("Missing required field");
                }
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}

public function schedule_session() {
    try {
        if (isset($_POST['userId']) && isset($_POST['co_email']) && isset($_POST['co_img']) &&  isset($_POST['co_FirstName']) && isset($_POST['co_LastName']) && isset($_POST['channel_name']) && isset($_POST['topic']) && isset($_POST['category']) && isset($_POST['date']) && isset($_POST['time'])&& isset($_POST['formatteddate'])) {
            $query = "INSERT INTO schedule_session (userId,co_email,co_img,co_FirstName,co_LastName,channel_name,topic,category,date,time,formatteddate) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            if ($stmt = $this->con->prepare($query)) {
                if ($stmt->bind_param("issssssssss", $_POST['userId'],$_POST['co_email'] ,$_POST['co_img'], $_POST['co_FirstName'], $_POST['co_LastName'], $_POST['channel_name'] , $_POST['topic'] , $_POST['category'] , $_POST['date'], $_POST['time'], $_POST['formatteddate'])) {
                    if ($stmt->execute()) {
                        $arr = array(
                            'status' => true,
                        );
                        echo json_encode($arr);
                    } else {
                        throw new Exception("Query execution failed");
                    }
                } else {
                    throw new Exception("query binding failed");
                }
            } else {
                throw new Exception("prepare statement failed");
            }
        } else {
            throw new Exception("missing required field");
        }
        $stmt->close();
    } catch (Exception $ex) {
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
        die();
    }
}


public function fetch_session()
{
        try{
                $query = "SELECT * FROM schedule_session order by id DESC";
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }

                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                $rows = $stmt->get_result();
                $counsellor = array();
                while($row = $rows->fetch_assoc())  
                {
                  $counsellor[] = $row;
                }
                $result = array(
                  'status'=>true,
                  'counsellor'=>$counsellor
                );
                echo json_encode($result);
                $stmt->close();
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}

public function reward_points() {
    try {
        if ( isset($_POST['userId']) && isset($_POST['rewards_point']) && isset($_POST['rewards_number'])) {
            $query = "INSERT INTO reward_points (userId,rewards_point,reward_number) VALUES(?,?,?)";
            if ($stmt = $this->con->prepare($query)) {
                if ($stmt->bind_param("iii",$_POST['userId'] , $_POST['rewards_point'], $_POST['rewards_number'])) {
                    if ($stmt->execute()) {
                        $arr = array(
                            'status' => true,
                        );
                        echo json_encode($arr);
                    } else {
                        throw new Exception("Query execution failed");
                    }
                } else {
                    throw new Exception("query binding failed");
                }
            } else {
                throw new Exception("prepare statement failed");
            }
        } else {
            throw new Exception("missing required field");
        }
        $stmt->close();
    } catch (Exception $ex) {
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
        die();
    }
}
public function fetch_rewards_point()
{
        try{
                $query = "SELECT * FROM reward_points where userId='".$_POST['userId']."' order by id desc";
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }

                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                $rows = $stmt->get_result();
                $reward_point = array();
                while($row = $rows->fetch_assoc())  
                {
                  $reward_point[] = $row;
                }
                $result = array(
                  'status'=>true,
                  'reward_point'=>$reward_point
                );
                echo json_encode($result);
                $stmt->close();
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}

public function fetch_top_rewards()
{
        try{
                $query = "SELECT * FROM `reward_points` ORDER by rewards_point DESC LIMIT 5";
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }

                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                $rows = $stmt->get_result();
                $users = array();
                while($row = $rows->fetch_assoc())  
                {
                  $users[] = $row;
                }
                $result = array(
                  'status'=>true,
                  'users'=>$users
                );
                echo json_encode($result);
                $stmt->close();
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}


public function UpdateRewards()
{
        try{
            if(isset($_POST['user_id']))
            {
                $query = "UPDATE reward_points set rewards_point = '".$_POST['rewards_point']."' , reward_number = '".$_POST['reward_number']."' , name = '".$_POST['name']."' WHERE userId='".$_POST['user_id']."'";
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }

                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                
                $result = array(
                  'status'=>true,
                  'message'=>"Updated Successfully"
                );
                echo json_encode($result);
                $stmt->close();
           }else {
                  throw new Exception("Missing required field");
                }
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}





public function checkDeviceIdExist(){

	try{

		$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

		$response=array();
		

		$queryCheckDeviceId="SELECT `deviceId` FROM `users` WHERE `deviceId`='".$jsonObj['deviceId']."'";

											   
	

	   $resultCheckDeviceId=$this->con->query($queryCheckDeviceId);

	   if ($resultCheckDeviceId->num_rows>0) {

		
		$response['status']=true;
		$response['deviceIdExist']=true;


		}else{

			$response['status']=true;
			$response['deviceIdExist']=false;

		}



   echo json_encode($response);

}catch(Exception $ex){
	   $result = array(
		   'status' => false,
		   'msg' => $ex->getMessage()
	   );
	   echo json_encode($result);
	 }


   }







public function updateDeviceID()
{

	$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

        try{
            if(isset($jsonObj['userId'])&isset($jsonObj['deviceId']))
            {
                $query = "UPDATE `users` SET `deviceId`='".$jsonObj['deviceId']."' WHERE `id`=".$jsonObj['userId'];
                if(!$stmt = $this->con->prepare($query))
                {
                  throw new Exception("Prepare statement failed : ".$this->con->error);
                }

                if(!$stmt->execute())
                {
                  throw new Exception("Query execution failed : ".$stmt->error);
                }
                
                $result = array(
                  'status'=>true,
                  'message'=>"Updated Successfully"
                );
                echo json_encode($result);
                $stmt->close();
           }else {
                  throw new Exception("Missing required field");
                }
        }
            catch(Exception $ex){
                $result = array(
                    'status' => false,
                    'msg' => $ex->getMessage()
                );
                echo json_encode($result);
            }
}







public function fetchOnToOnePackage()
{
	$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

	$response=array();
	$batchSlotArray=array();
	$responseDataData=array();


      try{

      	$response['success']=true;
		$response['package_desc']="1.You'll be getting 45 minutes of personal LIVE video Call + Chat.\n2.One of our top counselor will be assigned to you before the payment is complete.\n3.Book a slot which is convenient to you.\n4.Batches a\n    1.Morning batch      ->9AM-12PM\n    2.Afternoon batch   ->12PM-3PM\n    3.Evening batch       ->3PM-6PM";



		$discount1['coupon1']="FIRSTCS";
		$discount1['price']="200";

		$discount2['coupon2']="ABC1234";
		$discount2['price']="100";

		$allDiscount=array();

		array_push($allDiscount, $discount1);
		array_push($allDiscount, $discount2);


		$response['discount_coupon']=$allDiscount;

		$response['packageCost']="1999";

   		$response['packageName']="One To One";

   	

				$queryFetchAllAllDays="SELECT * FROM `day_table`";
      			$resultFetchAllAllDays=$this->con->query($queryFetchAllAllDays);

      			$allDaysArray=array();
      			
      			while ($eachDay = $resultFetchAllAllDays->fetch_assoc()) {
      				
      				$eachDayArray=array();	

      				if( $eachDay['day_id']==0){
      					$_date    = date('l d M y');
	      			}else{
	      				$_date    = date('l d M y', strtotime(date('Y-m-d') .' +'. $eachDay['day_id'].' day'));	
	      			}
      			
	      			$queryFetchAllBatches="SELECT * FROM `batch_slot`";
	      			$resultAllBatches=$this->con->query($queryFetchAllBatches);
	      				

	      			$batchSlotArray=array();
					while ($eachBatch = $resultAllBatches->fetch_assoc()) {

							//$eachBatchobj=new array();

							$eachBatchobj['batchNo']=$eachBatch['batch_id'];
							$eachBatchobj['batchTiming']=$eachBatch['batch_timing'];



							//$timeSlotArray=new array();

							$queryAllTimeSlots="SELECT time_slot FROM `time_slot` WHERE fk_batch_id=".$eachBatch['batch_id'];

							$timeSlotArray=array();

							$resultTimeSlot=$this->con->query($queryAllTimeSlots);

							while ($eachTimeSlot =$resultTimeSlot->fetch_assoc()){		


										
								
								$queryCheckAvailable="SELECT `".$eachTimeSlot['time_slot']."` as counselorId FROM `available_counselor` WHERE `".$eachTimeSlot['time_slot']."`!=0 and fk_day_id=".$eachDay['day_id'];


								$resultCheckAvailable=$this->con->query($queryCheckAvailable);




								if($resultCheckAvailable->num_rows>0){//if there are counselors check if they are booked 


										$counselorArray=array();

										while($eachCounselorId=$resultCheckAvailable->fetch_assoc()){

											$queryCheckCounselorAvailable="SELECT fk_co_id FROM `one_to_one_bookings` WHERE fk_co_id=".$eachCounselorId['counselorId']." and `dateBooked`='".$_date."' AND `timeSlot`='".$eachTimeSlot['time_slot']."'";

											
											$resultCounselorAvailable=$this->con->query($queryCheckCounselorAvailable);

											if ($resultCounselorAvailable->num_rows==0) {//if count is higher than 0 means they are booked

												$queryFetchCounselorExpertLevel="SELECT `student_education_level`,`first_name`,`last_name`,`profile_pic`,`email` FROM `co_user` WHERE `id`=".$eachCounselorId['counselorId'];
												
												$resulFetchCounselorExpertLevel=$this->con->query($queryFetchCounselorExpertLevel);

												$resulFetchCounselorExpertLevel=$resulFetchCounselorExpertLevel->fetch_assoc();

												$eachCounselorObj['co_id']=$eachCounselorId['counselorId'];
												$eachCounselorObj['co_FullName']=$resulFetchCounselorExpertLevel['first_name']." ".$resulFetchCounselorExpertLevel['last_name'];

												$eachCounselorObj['profile_pic']=$resulFetchCounselorExpertLevel['profile_pic'];
												$eachCounselorObj['email']=$resulFetchCounselorExpertLevel['email'];

												$eachCounselorObj['expertLevel']=$resulFetchCounselorExpertLevel['student_education_level'];

												array_push($counselorArray,$eachCounselorObj);


												
											}


										}

										if (count($counselorArray)>0) {
											$eachTimeSlot['available']=true;
											$eachTimeSlot['available_counselor']=$counselorArray;

										}else{
											$eachTimeSlot['available']=false;
										}


								}else{
									$eachTimeSlot['available']=false;
								}


								

								array_push($timeSlotArray, $eachTimeSlot);
							}

							$eachBatchobj['bookingSlot']=$timeSlotArray;

							array_push($batchSlotArray,$eachBatchobj);
			

      			}

				$eachDayArray['day_name']=$_date;
      			$eachDayArray['batch_data']= $batchSlotArray;

      		
      			array_push($allDaysArray,$eachDayArray);

      		}

      	$response['batchSlot']=$allDaysArray;

      	echo json_encode($response);


      }
      catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }
}



public function bookOneToOne(){

	$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

	$co_id=$jsonObj["co_id"];
	$student_id=$jsonObj["student_id"];
	$student_name=$jsonObj["student_name"];
	$student_email=$jsonObj["student_email"];
	$date_booked=$jsonObj["date_booked"];
	$time_slot=$jsonObj["time_slot"];
	$price=$jsonObj["price"];
	$discount_availed=$jsonObj["discount_availed"];
	$confirmed_booking=$jsonObj["confirmed_booking"];
	$channel_name=$jsonObj["channel_name"];
	$category=$jsonObj["category"];


	try{

		$queryCheckSlotBooked="SELECT fk_co_id FROM `one_to_one_bookings` WHERE fk_co_id=".$co_id." and `dateBooked`='".$date_booked."' AND `timeSlot`='".$time_slot."'";


											
				$resultCheckSlotBooked=$this->con->query($queryCheckSlotBooked);

				if ($resultCheckSlotBooked->num_rows==0) {//if count is higher than 0 means slot is booked


					$stmt=$this->con->prepare("INSERT INTO `one_to_one_bookings` ( `fk_co_id`, `fk_student_id`,`studentName`,`studentEmail`, `dateBooked`, `timeSlot`, `price`, `discountAvailed`, `confirmedBooking`, `channelName`,`category`) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
							
								$stmt->bind_param("iissssiiiss",$co_id, $student_id, $student_name,$student_email,$date_booked, $time_slot,$price,$discount_availed,$confirmed_booking,$channel_name,$category);
							//si: s is string :i is integer

								if($stmt->execute()){

									$queryfetchBookingId="SELECT `id` FROM `one_to_one_bookings` WHERE `fk_co_id`=".$co_id." and `dateBooked`='".$date_booked."' and `fk_student_id`=".$student_id." and `timeSlot` ='".$time_slot."' ";

								
									$resultfetchBookingId=$this->con->query($queryfetchBookingId);
									$resultfetchBookingIdd=$resultfetchBookingId->fetch_assoc();

									$response=array(
									'status' => true,
									'message' => 'Session Booked!',
									'booking_id'=>$resultfetchBookingIdd['id']);
									
									echo json_encode($response);
								}

					}else{
									$response=array(
									'status' => false,
            						'message' => 'Sorry this slot is already booked!');
									
									echo json_encode($response);
					}





		}catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }



}


 public function fetchOneToOneBookingsForCounselor(){

 	try{

 		$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

	 	$co_id=$jsonObj["co_id"];

	 	$response=array();
	 	$bookingsArray=array();

	 	$queryfetchBookings="SELECT `id`,`sessionHeld`,`fk_student_id`,`channelName`,`category`,`dateBooked`,`timeSlot`,`category`,`studentName` FROM `one_to_one_bookings` WHERE fk_co_id=".$co_id;

												
	 	

		$resultfetchBookings=$this->con->query($queryfetchBookings);

		if ($resultfetchBookings->num_rows>0) {

			while($eachBooking=$resultfetchBookings->fetch_assoc()){

					$eachBookingData['booking_id']=$eachBooking['id'];
					$eachBookingData['sessionHeld']=$eachBooking['sessionHeld'];
					$eachBookingData['studentName']=$eachBooking['studentName'];
					$eachBookingData['dateBooked']=$eachBooking['dateBooked'];
					$eachBookingData['timeSlot']=$eachBooking['timeSlot'];
					$eachBookingData['channelName']=$eachBooking['channelName'];
					$eachBookingData['category']=$eachBooking['category'];
				
				array_push($bookingsArray,$eachBookingData);

	 	}

	 	$response['status']=true;
	 	$response['data']=$bookingsArray;


	}else{
		$response['status']=true;
	 	$response['data']=$bookingsArray;
	}



	echo json_encode($response);

}catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }



	}


	public function saveOneToOneChat(){

		$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

		$fk_booking_id=$jsonObj["fk_booking_id"];
		$host_id=$jsonObj["host_id"];
		$student_id=$jsonObj["student_id"];
		$sender_id=$jsonObj["sender_id"];
		$message=$jsonObj["message"];
		$time_stamp=$jsonObj["time_stamp"];`
        $date_sent=$jsonObj["date_sent"];
        

		$response=array();

		$stmt=$this->con->prepare("INSERT INTO `one_to_one_chat_history`(`fk_booking_id`, `host_id`, `student_id`, `sender_id`, `message`, `time_stamp`, `date_sent`) VALUES (?,?,?,?,?,?,?)");
			
						$stmt->bind_param("iiiisss",$fk_booking_id,$host_id,$student_id,$sender_id,$message,$time_stamp,$date_sent);
						//si: s is string :i is integer

			
						if($stmt->execute()){

							$response['status']=true;
	 						$response['data']="inserted message";

							echo json_encode($response);
						}
	}

	public function fetchOneToOneChatHistory(){

		$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

		try{

		$fk_booking_id=$jsonObj["fk_booking_id"];

	 	$response=array();
	 	$chatArray=array();

	 	$queryfetchChatHistory="SELECT `fk_booking_id`, `host_id`, `student_id`, `sender_id`, `message`, `time_stamp`, `date_sent` FROM `one_to_one_chat_history` where fk_booking_id=".$fk_booking_id;

											

		$resultfetchChatHistorys=$this->con->query($queryfetchChatHistory);

		if ($resultfetchChatHistorys->num_rows>0) {

			while($eachMessage=$resultfetchChatHistorys->fetch_assoc()){

				array_push($chatArray,$eachMessage);

	 		}

	 	$response['status']=true;
	 	$response['data']=$chatArray;


	}else{
		$response['status']=true;
	 	$response['data']=$chatArray;
	}



	echo json_encode($response);

}catch(Exception $ex){
        $result = array(
            'status' => false,
            'msg' => $ex->getMessage()
        );
        echo json_encode($result);
      }


	}


	public function fetchTime(){
        $response=array();
        date_default_timezone_set("Asia/Kolkata");

        $response['status']=true;
        $response['date']=date('l d M y');
        $AMPM=strtoupper(date("a"));
        $response['time']=date('H:i').$AMPM;

        echo json_encode($response);
    }


	
	
	public function fetchOneToOneBookingsForStudent(){

		try{
   
			$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed
   
			$fk_student_id=$jsonObj["fk_student_id"];
   
			$response=array();
			$bookingsArray=array();
   
			$queryfetchBookings="SELECT `id`,`sessionHeld`,`fk_student_id`,`channelName`,`category`,`dateBooked`,`timeSlot`,`category`,`fk_co_id`,`sessionHeld`,`studentName`,`videoUrl` FROM `one_to_one_bookings` WHERE fk_student_id=".$fk_student_id." and confirmedBooking='1'";
   
												   
		
   
		   $resultfetchBookings=$this->con->query($queryfetchBookings);
   
		   if ($resultfetchBookings->num_rows>0) {
   
			   while($eachBooking=$resultfetchBookings->fetch_assoc()){
   


   
				   $queryfetchCounselorDetails="Select `first_name`,`last_name`,`profile_pic` from co_user where id=".$eachBooking['fk_co_id'];
   
				   $resultfetchCounselorDetails=$this->con->query($queryfetchCounselorDetails);
				   $resultfetchCounselorDetails=$resultfetchCounselorDetails->fetch_assoc();
   
				   	   $eachBookingData['booking_id']=$eachBooking['id'];
					   $eachBookingData['sessionHeld']=$eachBooking['sessionHeld'];
					   $eachBookingData['studentName']=$eachBooking['studentName'];
					   $eachBookingData['counselorName']=$resultfetchCounselorDetails['first_name'].' '.$resultfetchCounselorDetails['last_name'];
					   $eachBookingData['dateBooked']=$eachBooking['dateBooked'];
					   $eachBookingData['timeSlot']=$eachBooking['timeSlot'];
					   $eachBookingData['channelName']=$eachBooking['channelName'];
					   $eachBookingData['category']=$eachBooking['category'];
					   $eachBookingData['sessionHeld']=$eachBooking['sessionHeld'];
					   $eachBookingData['videoUrl']=$eachBooking['videoUrl'];
					   $eachBookingData['profile_pic']='https://app.careerguide.com/api/user_dir/'.$resultfetchCounselorDetails['profile_pic'];
				   
				   array_push($bookingsArray,$eachBookingData);
   
			}
   
			$response['status']=true;
			$response['data']=$bookingsArray;
   
   
	   }else{
		   $response['status']=true;
			$response['data']=$bookingsArray;
	   }
   
   
   
	   echo json_encode($response);
   
   }catch(Exception $ex){
		   $result = array(
			   'status' => false,
			   'msg' => $ex->getMessage()
		   );
		   echo json_encode($result);
		 }

   
	   }

	   public function updateOneToOneDeepLink(){

		$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

		$stmt=$this->con->prepare("UPDATE `one_to_one_bookings` SET `deeplink`='".$jsonObj["deeplink"]."' WHERE id=".$jsonObj["booking_id"]);
			
		//si: s is string :i is integer
						if($stmt->execute()){
							$responseData["success"]=true;
							$responseData["successMessage"]="Deeplink updated successfully";

							echo json_encode($responseData);


						}
						else
						{
							$responseData["success"]=false;
							$responseData["errorMessage"]="Some error occured!";

							

							echo json_encode($responseData);
						}

	   }


	   public function updateOneToOneFeedback(){

		$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

		

		$stmt=$this->con->prepare("UPDATE `one_to_one_bookings` SET `overallRating`=".$jsonObj["overallRating"].",`recommendMessage`='".$jsonObj["recommendMessage"]."',`comments`='".$jsonObj["comments"]."' WHERE id=".$jsonObj["booking_id"]);
					
						//si: s is string :i is integer
						if($stmt->execute()){
							$responseData["success"]=true;
							$responseData["successMessage"]="Feedback updated successfully";

						
							echo json_encode($responseData);

						}
						else
						{
							$responseData["success"]=false;
							$responseData["errorMessage"]="Some error occured!";

							echo json_encode($responseData);
						}

	   }
   
	   
	   public function updateOneToOneSessionHeld(){

		$jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

		$stmt=$this->con->prepare("UPDATE `one_to_one_bookings` SET `sessionHeld`='".$jsonObj["sessionHeld"]."' WHERE id=".$jsonObj["booking_id"]);
			
		//si: s is string :i is integer
						if($stmt->execute()){
							$responseData["success"]=true;
							$responseData["successMessage"]="Session updated successfully";

						
							echo json_encode($responseData);

						}
						else
						{
							$responseData["success"]=false;
							$responseData["errorMessage"]="Some error occured!";


							echo json_encode($responseData);
						}

	   }


	   public function updateOneToOneBookingConfirmed(){

        $jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed



        if($jsonObj["confirmedBooking"]=="0"){

            $stmt=$this->con->prepare("DELETE FROM one_to_one_bookings WHERE id=".$jsonObj["booking_id"]);
            if($stmt->execute()){
                $responseData["success"]=true;
                $responseData["successMessage"]="Session deleted successfully";

                echo json_encode($responseData);


        }
    }else{

            $stmt=$this->con->prepare("UPDATE one_to_one_bookings SET confirmedBooking='".$jsonObj["confirmedBooking"]."' WHERE id=".$jsonObj["booking_id"]);

            //si: s is string :i is integer
                        if($stmt->execute()){
                            $responseData["success"]=true;
                            $responseData["successMessage"]="Booking updated successfully";

			    $queryfetchBookingDetails ="SELECT `id`,`fk_co_id`,`studentName`,`studentEmail`,`dateBooked`,`timeSlot`,`deeplink`,`price` FROM `one_to_one_bookings` WHERE `id`=".$jsonObj["booking_id"];
   

			   $resultfetchBookingDetails=$this->con->query($queryfetchBookingDetails);
   
			   if ($resultfetchBookingDetails->num_rows>0) {
   
			   if($booking=$resultfetchBookingDetails->fetch_assoc()){
				
				   $queryfetchCounselorDetails="Select `first_name`,`last_name`,`email` from co_user where id=".$booking['fk_co_id'];
   
				   $resultfetchCounselorDetails=$this->con->query($queryfetchCounselorDetails);
				   $resultfetchCounselorDetails=$resultfetchCounselorDetails->fetch_assoc();
   


				$user=$booking['studentEmail'];
				$subject = "CareerGuide-Booking details";
                        	$msgstudent = "<h3>Hi " . $booking['studentName'] . ",</h3>";
                        	$msgstudent .= "<p>Thanks for booking with CareerGuide! </p><br>
					<p>Click on the link <a href='".$booking['deeplink']."' > '".$booking['deeplink']."' </a> to access the session directly.<br>
					You can open the app and navigate to Homescreen->Profile->Bookings</p><br><br>
					 <p>Booking for ".$booking['studentName']."</p><br>
					 <p>Date        ".$booking['dateBooked']."</p><br>
					 <p>Time        ".$booking['timeSlot']."</p><br>
					 <p>Counselor   ".$resultfetchCounselorDetails['first_name']." ".$resultfetchCounselorDetails['last_name']."</p><br>
					 <p>Category    ".$booking['category']."</p><br><br>

                                         <p>
                                         Thanks,<br>
                            		CareerGuide Admin.
                            		</p>";


				$msgcounselor = "<h3>Hi " . $resultfetchCounselorDetails['first_name']." ".$resultfetchCounselorDetails['last_name'] . ",</h3>";
				$msgcounselor .= "<p>You got a new booking from CareerGuide! </p><br>
					
					 <p>Booking for ".$booking['studentName']."</p><br>
					 <p>Date        ".$booking['dateBooked']."</p><br>
					 <p>Time        ".$booking['timeSlot']."</p><br>
					 <p>Counselor   ".$resultfetchCounselorDetails['first_name']." ".$resultfetchCounselorDetails['last_name']."</p><br>
					 <p>Category    ".$booking['category']."</p><br><br>

                                         <p>
                                         Thanks,<br>
                            		CareerGuide Admin.
                            		</p>";



                        	$responce = self::send_mail_fun($booking['studentName'],$booking['studentEmail'] , $subject, $msgstudent);

				$responce = self::send_mail_fun($resultfetchCounselorDetails['first_name'], $resultfetchCounselorDetails['email'], $subject, $msgcounselor);
			}

			    echo json_encode($responseData);

                        }
                        else
                        {
                            $responseData["success"]=false;
                            $responseData["errorMessage"]="Some error occured!";

                            echo json_encode($responseData);

                        }
            }

       }
    }
	public function updateOneToOneVideoUrl(){

        $jsonObj = json_decode(file_get_contents('php://input'), true);//gets the json object passed

        $stmt=$this->con->prepare("UPDATE one_to_one_bookings SET videoUrl='".$jsonObj["videoUrl"]."' WHERE id=".$jsonObj["booking_id"]);

        //si: s is string :i is integer
                        if($stmt->execute()){
                            $responseData["success"]=true;
                            $responseData["successMessage"]="Booking video url updated successfully";


                            echo json_encode($responseData);

                        }
                        else
                        {
                            $responseData["success"]=false;
                            $responseData["errorMessage"]="Some error occured!";
				
			    echo json_encode($responseData);


                        }

	   }
	   






}
?>
